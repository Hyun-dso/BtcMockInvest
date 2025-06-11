function loadPosts() {
  fetch(window.contextPath + '/post/list')
    .then(res => res.json())
    .then(posts => {
      const container = document.getElementById('posts-container');
      if (!container) return;
      container.innerHTML = '';
      posts.forEach(p => {
        const postDiv = document.createElement('div');
        postDiv.className = 'post';
        postDiv.style.border = '1px solid #ddd';
        postDiv.style.marginBottom = '15px';
        postDiv.style.padding = '10px';
        postDiv.innerHTML = `
          <p><strong>작성자:</strong> ${p.userId}</p>
          <p><strong>제목:</strong> ${p.title}</p>
          <p>${p.content}</p>
          <span class="like-count">좋아요 ${p.likeCount}</span>
          <p class="toggle-comments" data-id="${p.postId}" style="color: blue; cursor: pointer;">댓글 보기</p>
          <div class="comments" style="display: none; margin-top: 10px; padding-left: 10px;"></div>
        `;
        container.appendChild(postDiv);
      });
      attachCommentEvents();
    });
}

function attachCommentEvents() {
  document.querySelectorAll('.toggle-comments').forEach(toggle => {
    toggle.onclick = function () {
      const comments = this.nextElementSibling;
      const postId = this.dataset.id;
      const isOpen = comments.style.display === 'block';
      if (!isOpen && !comments.dataset.loaded) {
        fetch(window.contextPath + '/post/' + postId + '/comments')
          .then(res => res.json())
          .then(list => {
            comments.innerHTML = '';
            list.forEach(c => {
              const item = document.createElement('div');
              item.className = 'comment-item';
              item.style.borderTop = '1px solid #eee';
              item.style.paddingTop = '5px';
              item.innerHTML = `<p><strong>${c.userId}:</strong> ${c.content}</p>`;
              comments.appendChild(item);
            });
            comments.dataset.loaded = 'true';
            comments.style.display = 'block';
            toggle.textContent = '댓글 닫기';
          });
      } else {
        comments.style.display = isOpen ? 'none' : 'block';
        toggle.textContent = isOpen ? '댓글 보기' : '댓글 닫기';
      }
    };
  });
}

document.addEventListener('DOMContentLoaded', function () {
  loadPosts();
  setInterval(loadPosts, 5000);
});
