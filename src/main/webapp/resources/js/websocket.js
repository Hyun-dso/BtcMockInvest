// contextPath 전역 설정
window.contextPath = document.body.getAttribute("data-context");

window.websocket = {
  stompClient: null,
  connected: false,
  callbacks: [],

  connect: function (callback) {
    if (this.connected) {
      console.log("🔁 이미 연결됨 → 콜백만 실행");
      this.callbacks.push(callback);
      callback(this.stompClient);
      return;
    }

    this.callbacks.push(callback);

    const socket = new SockJS(window.contextPath + "/ws-endpoint");
    this.stompClient = Stomp.over(socket);

    this.stompClient.connect({}, () => {
      this.connected = true;
      console.log("✅ WebSocket 최초 연결 완료");

      this.callbacks.forEach(cb => cb(this.stompClient));
    });
  }
};
