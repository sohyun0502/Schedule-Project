package kr.spartaclub.scheduleproject.controller;

import jakarta.validation.Valid;
import kr.spartaclub.scheduleproject.dto.comment.CreateCommentRequest;
import kr.spartaclub.scheduleproject.dto.comment.CreateCommentResponse;
import kr.spartaclub.scheduleproject.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    // 댓글 생성
    @PostMapping("/schedules/{scheduleId}/comments")
    public ResponseEntity<CreateCommentResponse> createComment(
            @RequestAttribute Long userId,
            @PathVariable Long scheduleId,
            @Valid @RequestBody CreateCommentRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(commentService.saveComment(userId, scheduleId, request));
    }
}
