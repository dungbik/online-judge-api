package yoonleeverse.onlinejudge.api.submission.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.support.PageableExecutionUtils;
import yoonleeverse.onlinejudge.api.submission.dto.GetAllCommentRequest;
import yoonleeverse.onlinejudge.api.submission.entity.Comment;
import yoonleeverse.onlinejudge.api.submission.entity.Submission;
import yoonleeverse.onlinejudge.util.NumberUtil;

import java.util.List;

import static yoonleeverse.onlinejudge.api.common.constant.Constants.COMMENT_MAX_SIZE;

@RequiredArgsConstructor
public class CustomCommentRepositoryImpl implements CustomCommentRepository {

    private final MongoTemplate mongoTemplate;

    @Override
    public Page<Comment> getAllComment(String email, GetAllCommentRequest req) {
        int page = NumberUtil.toPage(req.getPage());

        Sort sort = Sort.by(Sort.Direction.DESC, "createdAt");
        Pageable pageable = PageRequest.of(page, COMMENT_MAX_SIZE, sort);

        Criteria criteria = Criteria.where("userId").is(email)
                .and("deleted").is(false);

        Query query = new Query(criteria).with(pageable);
        List<Comment> comments = this.mongoTemplate.find(query, Comment.class);
        Page<Comment> commentPage = PageableExecutionUtils.getPage(
                comments,
                pageable,
                () -> mongoTemplate.count(query.skip(-1).limit(-1), Comment.class)
        );

        return commentPage;

    }
}
