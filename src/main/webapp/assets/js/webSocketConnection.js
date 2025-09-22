const ws = new WebSocket("ws://localhost:8080/bookiecake/ws/updates");
ws.onopen = () => console.log("Connected");
ws.onmessage = (e) => console.log("Received:", e.data);
ws.onclose = () => console.log("Disconnected");
function sendMsg() { ws.send(document.getElementById("msg").value); }