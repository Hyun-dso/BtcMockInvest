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

        const commentsHtml = (p.comments || [])
          .map(c => `<div class="comment-item" style="border-top:1px solid #eee;padding-top:5px;"><strong>${c.username}:</strong> ${c.content}</div>`)
          .join('');

         postDiv.innerHTML = `
          <p><strong>작성자:</strong> ${p.username}</p>
           <p><strong>제목:</strong> ${p.title}</p>
           <p>${p.content}</p>
           <span class="like-count">좋아요 ${p.likeCount}</span>
           <div class="comments" style="margin-top:10px;padding-left:10px;">${commentsHtml}</div>
         `;
         container.appendChild(postDiv);
       });
     });
 }
 
 document.addEventListener('DOMContentLoaded', function () {
   loadPosts();
   setInterval(loadPosts, 5000);
 });
 
