let username = null;
let userRole = null;
let socket = null;
let stompClient = null;

// DOM Elements
const loginForm = document.getElementById('loginForm');
const usernameInput = document.getElementById('usernameInput');
const loginButton = document.getElementById('loginButton');
const mainContent = document.getElementById('mainContent');
const adminPanel = document.getElementById('adminPanel');
const auctionForm = document.getElementById('auctionForm');
const currentUserElement = document.getElementById('currentUser');
const userRoleElement = document.getElementById('userRole');
const auctionsContainer = document.getElementById('auctionsContainer');
const notificationsDiv = document.getElementById('notifications');

// Event Listeners
loginButton.addEventListener('click', login);
auctionForm.addEventListener('submit', createAuction);

function login() {
    username = usernameInput.value.trim();
    
    if (!username) {
        displayNotification('Please enter a username', true);
        return;
    }
    
    userRole = username === 'admin' ? 'admin' : 'client';
    
    loginForm.style.display = 'none';
    mainContent.style.display = 'block';
    
    if (userRole === 'admin') {
        adminPanel.style.display = 'block';
    }
    
    currentUserElement.textContent = `Logged in as: ${username}`;
    userRoleElement.textContent = `Role: ${userRole}`;
    
    connectToWebSocket();
}

function connectToWebSocket() {
    socket = new SockJS('/auction-ws');
    stompClient = Stomp.over(socket);
    
    stompClient.connect({}, onConnected, onError);
}

function onConnected() {
    // Subscribe to public auction topic
    stompClient.subscribe('/topic/auctions', onAuctionMessageReceived);
    
    // Subscribe to private user messages
    stompClient.subscribe(`/user/queue/private`, onPrivateMessageReceived);
    
    // Send login message
    sendMessage('/app/auction.login', { type: 'LOGIN', content: null });
    
    displayNotification('Connected to the auction system!');
}

function onError(error) {
    displayNotification('Could not connect to WebSocket server. Please refresh this page to try again!', true);
    console.error('WebSocket Error:', error);
}

function sendMessage(destination, messageBody) {
    if (stompClient) {
        const message = {
            sender: username,
            type: messageBody.type,
            content: messageBody.content
        };
        stompClient.send(destination, {}, JSON.stringify(message));
    } else {
        displayNotification('Not connected to WebSocket server', true);
    }
}

function onAuctionMessageReceived(payload) {
    const message = JSON.parse(payload.body);
    
    switch (message.type) {
        case 'AUCTIONS_LIST':
            displayAuctions(message.content);
            break;
        case 'NEW_AUCTION':
            addAuction(message.content);
            displayNotification(`New auction created: ${message.content.itemName}`);
            break;
        case 'AUCTION_UPDATE':
            updateAuction(message.content);
            displayNotification(`Auction updated: ${message.content.itemName}`);
            break;
        case 'AUCTION_EXPIRED':
            displayNotification(message.content);
            // Refresh the auction list
            sendMessage('/app/auction.getActiveAuctions', { type: 'GET_AUCTIONS', content: null });
            break;
        default:
            displayNotification(message.content);
    }
}

function onPrivateMessageReceived(payload) {
    const message = JSON.parse(payload.body);
    
    if (message.type === 'ERROR' || message.type === 'BID_ERROR') {
        displayNotification(message.content, true);
    } else {
        displayNotification(message.content);
    }
}

function displayAuctions(auctions) {
    auctionsContainer.innerHTML = '';
    
    if (!auctions || auctions.length === 0) {
        auctionsContainer.innerHTML = '<p>No active auctions available.</p>';
        return;
    }
    
    auctions.forEach(auction => {
        const auctionElement = createAuctionElement(auction);
        auctionsContainer.appendChild(auctionElement);
    });
}

function addAuction(auction) {
    // Clear "No active auctions" message if it exists
    if (auctionsContainer.innerHTML.includes('No active auctions available')) {
        auctionsContainer.innerHTML = '';
    }
    
    const auctionElement = createAuctionElement(auction);
    auctionsContainer.appendChild(auctionElement);
}

function updateAuction(updatedAuction) {
    const existingAuctionElement = document.getElementById(`auction-${updatedAuction.id}`);
    
    if (existingAuctionElement) {
        const newAuctionElement = createAuctionElement(updatedAuction);
        existingAuctionElement.replaceWith(newAuctionElement);
    } else {
        addAuction(updatedAuction);
    }
}

function createAuctionElement(auction) {
    const auctionElement = document.createElement('div');
    auctionElement.id = `auction-${auction.id}`;
    auctionElement.className = 'auction-card';
    
    if (auction.expired) {
        auctionElement.classList.add('auction-expired');
    }
    
    const expirationDate = new Date(auction.expirationTime);
    const formattedExpiration = formatDate(expirationDate);
    
    auctionElement.innerHTML = `
        <h3>${auction.itemName}</h3>
        <div class="auction-info">
            <p><strong>Current Price:</strong> $${auction.currentHighestBid.toFixed(2)}</p>
            <p><strong>Expires:</strong> ${formattedExpiration}</p>
            ${auction.winner ? `<p><strong>Winner:</strong> ${auction.winner}</p>` : ''}
        </div>
    `;
    
    if (!auction.expired && userRole !== 'admin') {
        const bidForm = document.createElement('div');
        bidForm.className = 'bid-form';
        bidForm.innerHTML = `
            <input type="number" class="bid-input" min="${auction.currentHighestBid + 0.01}" step="0.01" placeholder="Your bid...">
            <button class="bid-btn">Bid</button>
        `;
        
        const bidButton = bidForm.querySelector('.bid-btn');
        const bidInput = bidForm.querySelector('.bid-input');
        
        bidButton.addEventListener('click', () => {
            const bidAmount = parseFloat(bidInput.value);
            
            if (isNaN(bidAmount) || bidAmount <= auction.currentHighestBid) {
                displayNotification(`Bid must be greater than $${auction.currentHighestBid.toFixed(2)}`, true);
                return;
            }
            
            placeBid(auction.id, bidAmount);
            bidInput.value = '';
        });
        
        auctionElement.appendChild(bidForm);
    }
    
    return auctionElement;
}

function createAuction(event) {
    event.preventDefault();
    
    const itemName = document.getElementById('itemName').value.trim();
    const minPrice = parseFloat(document.getElementById('minPrice').value);
    const expirationMinutes = parseInt(document.getElementById('expirationMinutes').value);
    
    if (!itemName || isNaN(minPrice) || isNaN(expirationMinutes) || minPrice <= 0 || expirationMinutes <= 0) {
        displayNotification('Please fill all fields with valid values', true);
        return;
    }
    
    const auctionData = {
        itemName: itemName,
        minPrice: minPrice,
        expirationMinutes: expirationMinutes
    };
    
    sendMessage('/app/auction.create', { type: 'CREATE_AUCTION', content: auctionData });
    
    // Clear form
    document.getElementById('itemName').value = '';
    document.getElementById('minPrice').value = '';
    document.getElementById('expirationMinutes').value = '';
}

function placeBid(auctionId, amount) {
    const bidData = {
        auctionId: auctionId,
        amount: amount
    };
    
    sendMessage('/app/auction.bid', { type: 'PLACE_BID', content: bidData });
}

function displayNotification(message, isError = false) {
    const notificationElement = document.createElement('div');
    notificationElement.className = `notification ${isError ? 'error' : ''}`;
    notificationElement.textContent = message;
    
    notificationsDiv.appendChild(notificationElement);
    notificationsDiv.scrollTop = notificationsDiv.scrollHeight;
    
    // Auto remove after 5 seconds
    setTimeout(() => {
        notificationElement.remove();
    }, 5000);
}

function formatDate(date) {
    if (!(date instanceof Date)) {
        return 'Invalid date';
    }
    
    const now = new Date();
    const diff = date - now;
    
    // If expired
    if (diff <= 0) {
        return 'Expired';
    }
    
    // Convert to seconds, minutes, hours
    const seconds = Math.floor(diff / 1000);
    const minutes = Math.floor(seconds / 60);
    const hours = Math.floor(minutes / 60);
    
    if (hours > 24) {
        return date.toLocaleString();
    } else if (hours > 0) {
        return `${hours}h ${minutes % 60}m remaining`;
    } else if (minutes > 0) {
        return `${minutes}m ${seconds % 60}s remaining`;
    } else {
        return `${seconds}s remaining`;
    }
}
