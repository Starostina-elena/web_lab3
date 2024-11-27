function updateClock() {
    const now = new Date();
    document.getElementById('clock').innerText = now.toTimeString();
}

updateClock();
setInterval(updateClock, 9000);