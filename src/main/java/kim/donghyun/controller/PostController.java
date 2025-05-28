package kim.donghyun.controller;

import kim.donghyun.model.entity.Post;
import kim.donghyun.model.entity.Comment;
import kim.donghyun.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/post")
public class PostController {

    private final PostService postService;

    @Autowired
    public PostController(PostService postService) {
        this.postService = postService;
    }

    @GetMapping("/{id}")
    public String showPost(@PathVariable int id, Model model) {
        Post post = postService.getPostById(id);
        List<Comment> commentList = postService.getCommentsByPostId(id);

        model.addAttribute("post", post);
        model.addAttribute("commentList", commentList);
        return "postDetail";
    }
}
