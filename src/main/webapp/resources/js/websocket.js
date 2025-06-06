// contextPath 전역 설정
window.contextPath = document.body.getAttribute("data-context");

window.websocket = {
  stompClient: null,
  connected: false,
  connecting: false,
  callbacks: [],

  connect: function (callback) {
    if (this.connected) {
      console.log("🔁 이미 연결됨 → 콜백만 실행");
      this.callbacks.push(callback);
      callback(this.stompClient);
      return;
    }

    this.callbacks.push(callback);

	if (this.connecting) {
	  console.log("⏳ 연결 시도 중 → 콜백만 등록");
	  return;
	}

	this.connecting = true;
	
    const socket = new SockJS(window.contextPath + "/ws-endpoint");
    this.stompClient = Stomp.over(socket);

    this.stompClient.connect({}, () => {
      this.connected = true;
	  this.connecting = false;
      console.log("✅ WebSocket 최초 연결 완료");

      this.callbacks.forEach(cb => cb(this.stompClient));
    });
  }
};
