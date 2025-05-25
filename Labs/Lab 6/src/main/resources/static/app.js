let username = null;
let userRole = null;
let socket = null;
let stompClient = null;

// DOM Elements
const loginForm           = document.getElementById('loginForm');
const usernameInput       = document.getElementById('usernameInput');
const loginButton         = document.getElementById('loginButton');
const mainContent         = document.getElementById('mainContent');
const adminPanel          = document.getElementById('adminPanel');
const auctionForm         = document.getElementById('auctionForm');
const currentUserElement  = document.getElementById('currentUser');
const userRoleElement     = document.getElementById('userRole');
const auctionsContainer   = document.getElementById('auctionsContainer');
const notificationsDiv    = document.getElementById('notifications');

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

    loginForm.style.display       = 'none';
    mainContent.style.display     = 'block';
    if (userRole === 'admin') adminPanel.style.display = 'block';

    currentUserElement.textContent = `Logged in as: ${username}`;
    userRoleElement.textContent    = `Role: ${userRole}`;

    connectToWebSocket();
}

function connectToWebSocket() {
    socket      = new SockJS('/auction-ws');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, onConnected, onError);
}

function onConnected() {
    stompClient.subscribe('/topic/auctions', onAuctionMessageReceived);
    stompClient.subscribe('/user/queue/private', onPrivateMessageReceived);
    sendMessage('/app/auction.login', { type: 'LOGIN', content: null });
    displayNotification('Connected to the auction system!');

    // immediately fetch full list once
    pollAuctions();
    // then every 10 seconds
    setInterval(pollAuctions, 10_000);
}

function onError(error) {
    displayNotification('Could not connect to WebSocket server. Please refresh to try again!', true);
    console.error(error);
}

function sendMessage(destination, messageBody) {
    if (!stompClient) {
        displayNotification('Not connected to WebSocket server', true);
        return;
    }
    const msg = {
        sender:  username,
        type:    messageBody.type,
        content: messageBody.content
    };
    stompClient.send(destination, {}, JSON.stringify(msg));
}

function pollAuctions() {
    // tell server we want full, current list
    sendMessage('/app/auction.getActiveAuctions', { type: 'GET_AUCTIONS', content: null });
}


function onAuctionMessageReceived(payload) {
    const message = JSON.parse(payload.body);
    switch (message.type) {
        case 'AUCTIONS_LIST':
            displayAuctions(message.content);
            break;
        case 'NEW_AUCTION':
            addAuction(message.content);
            displayNotification(`New auction: ${message.content.itemName}`);
            break;
        case 'AUCTION_UPDATE':
            updateAuction(message.content);
            break;
        case 'AUCTION_EXPIRED':
            displayNotification(message.content);
            // re-fetch full list for safety
            pollAuctions();
            break;
        default:
            displayNotification(message.content);
    }
}

function onPrivateMessageReceived(payload) {
    const message = JSON.parse(payload.body);
    displayNotification(message.content, message.type.endsWith('_ERROR'));
}

function displayAuctions(auctions) {
    auctionsContainer.innerHTML = '';
    if (!auctions || auctions.length === 0) {
        auctionsContainer.innerHTML = '<p>No active auctions available.</p>';
        return;
    }
    auctions.forEach(auction => {
        auctionsContainer.appendChild(createAuctionElement(auction));
    });
}

function addAuction(auction) {
    if (auctionsContainer.textContent.includes('No active auctions')) {
        auctionsContainer.innerHTML = '';
    }
    auctionsContainer.appendChild(createAuctionElement(auction));
}

function updateAuction(auction) {
    const existing = document.getElementById(`auction-${auction.id}`);
    const el = createAuctionElement(auction);
    if (existing) existing.replaceWith(el);
    else auctionsContainer.appendChild(el);
}

function createAuctionElement(auction) {
    const el = document.createElement('div');
    el.id = `auction-${auction.id}`;
    el.className = 'auction-card';
    el.dataset.expiresAt = new Date(auction.expirationTime).getTime();

    if (auction.expired) el.classList.add('auction-expired');

    el.innerHTML = `
    <h3>${auction.itemName}</h3>
    <div class="auction-info">
      <p><strong>Current Price:</strong> $${auction.currentHighestBid.toFixed(2)}</p>
      <p class="expires"><strong>Expires:</strong> <span class="expires-text">${formatDate(new Date(auction.expirationTime))}</span></p>
      ${auction.winner ? `<p><strong>Winner:</strong> ${auction.winner}</p>` : ''}
    </div>
    ${!auction.expired && userRole !== 'admin' ? `
      <div class="bid-form">
        <input type="number" class="bid-input"
               min="${(auction.currentHighestBid + 0.01).toFixed(2)}"
               step="0.01" placeholder="Your bid...">
        <button class="bid-btn">Bid</button>
      </div>` : ''}
  `;

    if (!auction.expired && userRole !== 'admin') {
        const btn  = el.querySelector('.bid-btn');
        const input= el.querySelector('.bid-input');
        btn.addEventListener('click', () => {
            const amt = parseFloat(input.value);
            if (isNaN(amt) || amt <= auction.currentHighestBid) {
                displayNotification(`Bid must be > $${auction.currentHighestBid.toFixed(2)}`, true);
            } else {
                placeBid(auction.id, amt);
                input.value = '';
            }
        });
    }

    return el;
}

function createAuction(e) {
    e.preventDefault();
    const itemName          = document.getElementById('itemName').value.trim();
    const minPrice          = parseFloat(document.getElementById('minPrice').value);
    const expirationMinutes = parseInt(document.getElementById('expirationMinutes').value, 10);

    if (!itemName || isNaN(minPrice) || isNaN(expirationMinutes)
        || minPrice <= 0 || expirationMinutes <= 0) {
        displayNotification('Please fill all fields with valid values', true);
        return;
    }

    sendMessage('/app/auction.create', {
        type: 'CREATE_AUCTION',
        content: { itemName, minPrice, expirationMinutes }
    });

    auctionForm.reset();
}

function placeBid(id, amount) {
    sendMessage('/app/auction.bid', {
        type: 'PLACE_BID',
        content: { auctionId: id, amount }
    });
}

function displayNotification(msg, isError = false) {
    const d = document.createElement('div');
    d.className = `notification ${isError ? 'error' : ''}`;
    d.textContent = msg;
    notificationsDiv.appendChild(d);
    notificationsDiv.scrollTop = notificationsDiv.scrollHeight;
    setTimeout(() => d.remove(), 5000);
}

function formatDate(date) {
    if (!(date instanceof Date)) return 'Invalid date';
    const diff = date - new Date();
    if (diff <= 0) return 'Expired';
    const s = Math.floor(diff/1000), m = Math.floor(s/60), h = Math.floor(m/60);
    if (h > 24) return date.toLocaleString();
    if (h > 0)  return `${h}h ${m%60}m remaining`;
    if (m > 0)  return `${m}m ${s%60}s remaining`;
    return `${s}s remaining`;
}

// live local countdown
function updateTimers() {
    const now = Date.now();
    document.querySelectorAll('.auction-card').forEach(card => {
        const at = +card.dataset.expiresAt;
        const txt = card.querySelector('.expires-text');
        if (!txt) return;
        const d = at - now;
        if (d <= 0) {
            txt.textContent = 'Expired';
            card.classList.add('auction-expired');
        } else {
            const s = Math.floor(d/1000), m = Math.floor(s/60), h = Math.floor(m/60);
            if (h > 24)      txt.textContent = new Date(at).toLocaleString();
            else if (h > 0)  txt.textContent = `${h}h ${m%60}m remaining`;
            else if (m > 0)  txt.textContent = `${m}m ${s%60}s remaining`;
            else             txt.textContent = `${s}s remaining`;
        }
    });
}
setInterval(updateTimers, 1000);
