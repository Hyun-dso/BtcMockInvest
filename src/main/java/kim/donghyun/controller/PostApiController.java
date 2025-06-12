package kim.donghyun.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpSession;
import kim.donghyun.model.entity.Post;
import kim.donghyun.model.entity.User;
import kim.donghyun.service.PostService;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/posts")
public class PostApiController {

    private final PostService postService;

    @GetMapping
    public List<Post> list() {
        return postService.getAllPosts();
    }

    @PostMapping
    public ResponseEntity<?> create(HttpSession session,
                                    @RequestParam("content") String content) {
        User loginUser = (User) session.getAttribute("loginUser");
        if (loginUser == null) {
            return ResponseEntity.status(401).body("로그인이 필요합니다.");
        }
        Post post = postService.createPost(loginUser.getId(), loginUser.getUsername(), content);
        System.out.println("loginUser = " + loginUser);
        System.out.println("userId = " + (loginUser != null ? loginUser.getId() : "null"));
        return ResponseEntity.ok(post);
    }
}