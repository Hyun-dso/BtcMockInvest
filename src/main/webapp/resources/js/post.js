function appendPost(post) {
  const list = document.getElementById('postList');
  const div = document.createElement('div');
  div.className = 'post';
  div.style.border = '1px solid #ddd';
  div.style.marginBottom = '15px';
  div.style.padding = '10px';
  div.innerHTML = `<p><strong>제목:</strong> ${post.title}</p>
                    <p>${post.content}</p>`;
  list.prepend(div);
}

function loadPosts() {
  fetch(window.contextPath + '/api/posts')
    .then(r => r.json())
    .then(list => {
      document.getElementById('postList').innerHTML = '';
      list.forEach(p => appendPost(p));
    });
}

document.addEventListener('DOMContentLoaded', () => {
  loadPosts();

  window.websocket.connect(client => {
    client.subscribe('/topic/posts', message => {
      const post = JSON.parse(message.body);
      appendPost(post);
    });
  });

  const submit = document.getElementById('postSubmit');
  if (submit) {
    submit.addEventListener('click', () => {
      const title = document.getElementById('postTitle').value.trim();
      const content = document.getElementById('postContent').value.trim();
      if (!title || !content) {
        alert('제목과 내용을 입력하세요.');
        return;
      }
      fetch(window.contextPath + '/api/posts', {
        method: 'POST',
        headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
        body: new URLSearchParams({ title, content })
      }).then(res => {
        if (res.status === 401) {
          alert('로그인이 필요합니다.');
        } else if (res.ok) {
          document.getElementById('postTitle').value = '';
          document.getElementById('postContent').value = '';
        }
      });
    });
  }
});