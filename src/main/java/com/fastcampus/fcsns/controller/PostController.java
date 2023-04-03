package com.fastcampus.fcsns.controller;

import com.fastcampus.fcsns.controller.request.PostCreateRequest;
import com.fastcampus.fcsns.controller.request.PostModifyRequest;
import com.fastcampus.fcsns.controller.response.PostResponse;
import com.fastcampus.fcsns.controller.response.Response;
import com.fastcampus.fcsns.model.Post;
import com.fastcampus.fcsns.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;


    @PostMapping
    public Response<Void> create(@RequestBody PostCreateRequest request, Authentication authentication) {
        postService.create(request.getTitle(), request.getBody(), authentication.getName());

        return Response.success();
    }

    @PutMapping("/{postId}")
    public Response<PostResponse> modify(@RequestBody PostModifyRequest request, @PathVariable Integer postId, Authentication authentication) {
        Post post = postService.modify(request.getTitle(), request.getBody(), authentication.getName(), postId);

        return Response.success(PostResponse.fromPost(post));
    }

    @DeleteMapping("/{postId}")
    public Response<Void> delete(@PathVariable Integer postId, Authentication authentication) {
        postService.delete(authentication.getName(), postId);

        return Response.success();
    }
}
