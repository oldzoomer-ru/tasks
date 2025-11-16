package ru.gavrilovegor519.tasks.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import ru.gavrilovegor519.tasks.dto.input.comments.CreateCommentDto;
import ru.gavrilovegor519.tasks.dto.input.comments.EditCommentDto;
import ru.gavrilovegor519.tasks.dto.output.Response;
import ru.gavrilovegor519.tasks.dto.output.comments.CommentOutputDto;
import ru.gavrilovegor519.tasks.entity.Comments;
import ru.gavrilovegor519.tasks.exception.PaginationOutOfRangeException;
import ru.gavrilovegor519.tasks.mapper.CommentMapper;
import ru.gavrilovegor519.tasks.service.CommentsService;

@RestController
@RequestMapping("/api/1.0/comments")
@PreAuthorize("isAuthenticated()")
@AllArgsConstructor
public class CommentsController {
    private final CommentsService commentsService;
    private final CommentMapper commentMapper;

    @PostMapping("/create")
    @Operation(summary = "Create a comment")
    public ResponseEntity<Response<CommentOutputDto>> createComment(@Parameter(description = "Comment data", required = true)
                              @RequestBody @Valid CreateCommentDto comment,
                              Authentication authentication) {
        String authorEmail = authentication.getName();
        Comments comment1 = commentMapper.map(comment);

        Comments createdComment = commentsService.create(comment1, comment.getTaskId(), authorEmail);
        CommentOutputDto commentOutputDto = commentMapper.map(createdComment);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new Response<>(commentOutputDto, "Comment created successfully", true));
    }

    @PutMapping("/edit")
    @Operation(summary = "Edit a comment")
    public ResponseEntity<Response<CommentOutputDto>> editComment(@Parameter(description = "Comment changes", required = true)
                            @RequestBody @Valid EditCommentDto changes,
                            Authentication authentication) {
        String authorEmail = authentication.getName();
        Comments changes1 = commentMapper.map(changes);

        Comments updatedComment = commentsService.edit(changes.getCommentId(), changes1, authorEmail);
        CommentOutputDto commentOutputDto = commentMapper.map(updatedComment);

        return ResponseEntity.ok(new Response<>(commentOutputDto, "Comment updated successfully", true));
    }

    @DeleteMapping("/{id}/delete")
    @Operation(summary = "Delete a comment")
    public ResponseEntity<Response<String>> deleteComment(@Parameter(description = "ID of comment", required = true)
                              @PathVariable Long id,
                              Authentication authentication) {
        String authorEmail = authentication.getName();

        commentsService.delete(id, authorEmail);

        return ResponseEntity.ok(new Response<>("Comment deleted successfully", true));
    }

    @GetMapping("/get/{id}")
    @Operation(summary = "Get one comment by ID",
            responses = {
                    @ApiResponse(description = "The comment",
                            useReturnTypeSchema = true)
            })
    public ResponseEntity<Response<CommentOutputDto>> getComment(@PathVariable
                                       @Parameter(description = "ID of comment", required = true)
                                       Long id) {
        Comments comment = commentsService.getComment(id);
        CommentOutputDto commentOutputDto = commentMapper.map(comment);

        return ResponseEntity.ok(new Response<>(commentOutputDto, "Comment retrieved successfully", true));
    }

    @GetMapping("/get/user")
    public ResponseEntity<Response<Page<CommentOutputDto>>> getAllCommentsForUser(@RequestParam int start,
                                                        @RequestParam int end,
                                                        @RequestParam String email) {
        if ((end - start) < 1) {
            throw new PaginationOutOfRangeException("Out of range!");
        }

        Pageable pageable = PageRequest.of(start, end - start);
        Page<Comments> multipleCommentsForUser = commentsService.getMultipleCommentsForUser(email, pageable);

        Page<CommentOutputDto> commentOutputDtos = multipleCommentsForUser.map(commentMapper::map);

        return ResponseEntity.ok(new Response<>(commentOutputDtos, "Comments retrieved successfully", true));
    }

    @GetMapping("/get/task")
    public ResponseEntity<Response<Page<CommentOutputDto>>> getAllCommentsForTask(@RequestParam int start,
                                                        @RequestParam int end,
                                                        @RequestParam long taskId) {
        if ((end - start) < 1) {
            throw new PaginationOutOfRangeException("Out of range!");
        }

        Pageable pageable = PageRequest.of(start, end - start);
        Page<Comments> multipleCommentsForTask = commentsService.getMultipleCommentsForTask(taskId, pageable);

        Page<CommentOutputDto> commentOutputDtos = multipleCommentsForTask.map(commentMapper::map);

        return ResponseEntity.ok(new Response<>(commentOutputDtos, "Comments retrieved successfully", true));
    }
}
