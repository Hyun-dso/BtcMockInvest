function appendPost(post) {
	const list = document.getElementById('postList');
	const wrapper = document.createElement('div');
	const isMine = window.loginUserId && Number(post.userId) === Number(window.loginUserId);
	wrapper.className = `post-wrapper ${isMine ? 'mine' : 'other'}`;

	// ✅ 서버에서 전달된 시간이 있으면 사용하고, 없으면 현재 시간 사용
	const createdAt = post.createdAt
		? new Date(post.createdAt).toLocaleString()
		: new Date().toLocaleString();

	wrapper.innerHTML = `
			        <div class="post-username">${post.username}</div>
			        <div class="post ${isMine ? 'mine' : 'other'}">
			                <p>${post.content}</p>
			                <div class="timestamp">${createdAt}</div>
			        </div>
			`;

	const atBottom = list.scrollHeight - list.scrollTop <= list.clientHeight + 10;
	list.appendChild(wrapper);
	if (atBottom) {
		setTimeout(() => {
			list.scrollTop = list.scrollHeight;
		}, 0);
	}
}

function loadPosts() {
	fetch(window.contextPath + '/api/posts')
		.then(r => r.json())
		.then(list => {
			document.getElementById('postList').innerHTML = '';
			list.reverse().forEach(p => appendPost(p));
		});
}

document.addEventListener('DOMContentLoaded', () => {
	const isLoggedIn = document.body.getAttribute('data-logged-in') === 'true';
	const textarea = document.getElementById('postContent');
	const submitBtn = document.getElementById('postSubmit');
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
			if (!isLoggedIn) {
				window.location.href = window.contextPath + '/signin';
				return;
			}
			const content = document.getElementById('postContent').value.trim();
			if (!content) {
				showToast('내용을 입력하세요.');
				return;
			}
			fetch(window.contextPath + '/api/posts', {
				method: 'POST',
				headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
				body: new URLSearchParams({ content })
			}).then(res => {
				if (res.ok) {
					document.getElementById('postContent').value = '';
				}
			});
		});

	}
	if (textarea && submitBtn) {
		textarea.addEventListener('keydown', function(e) {
			if (e.key === 'Enter') {
				if (e.shiftKey) {
					// Shift+Enter는 줄바꿈
					return;
				} else {
					// Enter만 누르면 전송
					e.preventDefault();
					submitBtn.click();
				}
			}
		});
	}
});