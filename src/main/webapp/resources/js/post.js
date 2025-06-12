function appendPost(post) {
	const list = document.getElementById('postList');
	const div = document.createElement('div');
	div.className = 'post';
	div.style.border = '1px solid #ddd';
	div.style.marginBottom = '15px';
	div.style.padding = '10px';

	// ✅ 현재 시간 임시로 박기
	const createdAt = new Date().toLocaleString();

	// ✅ innerHTML 백틱(``)으로 감싸야 변수(${}) 적용됨!
	div.innerHTML = `
		<div class="post-header">익명 · ${createdAt}</div>
		<p><strong>제목:</strong> ${post.title}</p>
		<p>${post.content}</p>
	`;

	list.prepend(div);
}

function loadPosts() {
	fetch(window.contextPath + '/api/posts')
		.then(r => r.json())
		.then(list => {
			document.getElementById('postList').innerHTML = '';
			list.slice().reverse().forEach(p => appendPost(p));
		});
}

document.addEventListener('DOMContentLoaded', () => {
	const isLoggedIn = document.body.getAttribute('data-logged-in') === 'true';
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
				if (res.ok) {
					document.getElementById('postTitle').value = '';
					document.getElementById('postContent').value = '';
				}
			});
		});
	}
});