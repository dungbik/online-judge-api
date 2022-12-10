package yoonleeverse.onlinejudge.api.submission.service;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import yoonleeverse.onlinejudge.api.common.dto.APIResponse;
import yoonleeverse.onlinejudge.api.problem.entity.Problem;
import yoonleeverse.onlinejudge.api.problem.entity.ProgrammingLanguage;
import yoonleeverse.onlinejudge.api.problem.repository.ProblemRepository;
import yoonleeverse.onlinejudge.api.submission.dto.*;
import yoonleeverse.onlinejudge.api.submission.entity.Comment;
import yoonleeverse.onlinejudge.api.submission.entity.Like;
import yoonleeverse.onlinejudge.api.submission.entity.Submission;
import yoonleeverse.onlinejudge.api.submission.mapper.SubmissionMapper;
import yoonleeverse.onlinejudge.api.submission.repository.CommentRepository;
import yoonleeverse.onlinejudge.api.submission.repository.LikeRepository;
import yoonleeverse.onlinejudge.api.submission.repository.SubmissionRepository;
import yoonleeverse.onlinejudge.api.user.entity.UserEntity;
import yoonleeverse.onlinejudge.api.user.repository.UserRepository;
import yoonleeverse.onlinejudge.security.UserPrincipal;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SubmissionServiceImpl implements SubmissionService {
    private final SubmissionRepository submissionRepository;
    private final JudgeService judgeService;
    private final SubmissionMapper submissionMapper;
    private final ProblemRepository problemRepository;
    private final LikeRepository likeRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;

    @Override
    public SubmitProblemResponse submitProblem(UserPrincipal userPrincipal, SubmitProblemRequest req) {
        validateSubmitProblemReq(req);

        Submission submission = this.submissionMapper.toEntity(req);
        submission.setUserId(userPrincipal.getEmail());
        this.submissionRepository.save(submission);

        this.judgeService.judge(submission);

        return new SubmitProblemResponse();
    }

    @Override
    public GetAllSubmissionResponse getAllSubmission(UserPrincipal userPrincipal, GetAllSubmissionRequest req) {
        Page<Submission> submissionPage = this.submissionRepository.getAllSubmission(req);

        GetAllSubmissionResponse response = new GetAllSubmissionResponse();
        response.setPage(this.submissionMapper.toPageDto(submissionPage));

        if (!submissionPage.isEmpty()) {
            List<Submission> submissions = submissionPage.getContent();
            response.setSubmissions(this.submissionMapper.toDtoList(submissions));

            String submissionId = req.getSubmissionId();
            if (StringUtils.isNotEmpty(submissionId) && response.getSubmissions().size() == 1) {
                if (userPrincipal != null) {
                    String email = userPrincipal.getEmail();
                    boolean isLiked = likeRepository.findBySubmissionIdAndUserId(submissionId, email).isPresent();
                    response.getSubmissions().get(0).setLiked(isLiked);
                }
                List<CommentVO> comments = commentRepository.findAllBySubmissionIdAndDeleted(submissionId, false).stream()
                        .map((e) -> new CommentVO(e.getContent(), e.getUserId(), e.getCreatedAt()))
                        .collect(Collectors.toList());
                response.getSubmissions().get(0).setComments(comments);
            }

        }

        return response;
    }

    @Override
    public APIResponse addLike(String email, String submissionId) {

        boolean hasLike = likeRepository.findBySubmissionIdAndUserId(submissionId, email).isPresent();
        if (hasLike) {
            throw new RuntimeException("이미 좋아요를 누른 상태입니다.");
        }

        Submission submission = submissionRepository.findById(submissionId)
                .orElseThrow(() -> new RuntimeException("존재하지 않는 제출 이력입니다."));

        likeRepository.save(new Like(null, email, submissionId));

        submission.addLike();
        submissionRepository.save(submission);

        return new APIResponse();
    }

    @Override
    public APIResponse removeLike(String email, String submissionId) {

        Like like = likeRepository.findBySubmissionIdAndUserId(submissionId, email)
                .orElseThrow(() -> new RuntimeException("이미 좋아요가 취소된 상태입니다."));

        Submission submission = submissionRepository.findById(submissionId)
                .orElseThrow(() -> new RuntimeException("존재하지 않는 제출 이력입니다."));

        likeRepository.delete(like);

        submission.removeLike();
        submissionRepository.save(submission);

        return new APIResponse();
    }

    @Override
    public APIResponse addComment(String email, AddCommentRequest req) {
        String submissionId = req.getSubmissionId();

        UserEntity userEntity = userRepository.findById(email)
                .orElseThrow(() -> new RuntimeException("존재하지 않는 회원입니다."));

        Submission submission = submissionRepository.findById(submissionId)
                .orElseThrow(() -> new RuntimeException("존재하지 않는 제출 이력입니다."));

        Comment comment = Comment.of(req.getContent(), submissionId, email);
        commentRepository.save(comment);

        return new APIResponse();
    }

    @Override
    public APIResponse removeComment(String email, String commentId) {

        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new RuntimeException("존재하지 않는 댓글입니다."));

        if (!StringUtils.equals(comment.getUserId(), email)) {
            throw new RuntimeException("댓글 작성자가 아닙니다.");
        }

        comment.delete();
        commentRepository.save(comment);

        return new APIResponse();
    }

    @Override
    public APIResponse updateComment(String email, UpdateCommentRequest req) {
        String commentId = req.getCommentId();
        String content = req.getContent();

        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new RuntimeException("존재하지 않는 댓글입니다."));

        if (!StringUtils.equals(comment.getUserId(), email)) {
            throw new RuntimeException("댓글 작성자가 아닙니다.");
        }

        comment.update(content);
        commentRepository.save(comment);

        return new APIResponse();
    }


    private void validateSubmitProblemReq(SubmitProblemRequest req) {
        long problemId = req.getProblemId();
        ProgrammingLanguage language = req.getLanguage();
        if (language == null) {
            throw new RuntimeException("채점이 불가능한 프로그래밍 언어입니다.");
        }

        Problem problem = this.problemRepository.findById(problemId)
                .orElseThrow(() -> new RuntimeException("존재하지 않는 문제입니다."));

        boolean allowLanguage = problem.getLanguages().contains(language);
        if (!allowLanguage) {
            throw new RuntimeException("채점이 가능한 프로그래밍 언어가 아닙니다.");
        }

    }
}
