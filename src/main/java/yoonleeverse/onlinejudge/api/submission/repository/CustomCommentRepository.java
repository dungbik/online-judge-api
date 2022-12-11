package yoonleeverse.onlinejudge.api.submission.repository;

import org.springframework.data.domain.Page;
import yoonleeverse.onlinejudge.api.submission.dto.GetAllCommentRequest;
import yoonleeverse.onlinejudge.api.submission.entity.Comment;

public interface CustomCommentRepository {

    Page<Comment> getAllComment(String email, GetAllCommentRequest req);
}
