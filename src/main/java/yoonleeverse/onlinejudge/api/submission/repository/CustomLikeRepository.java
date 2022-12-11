package yoonleeverse.onlinejudge.api.submission.repository;

import org.springframework.data.domain.Page;
import yoonleeverse.onlinejudge.api.submission.dto.GetAllLikeRequest;
import yoonleeverse.onlinejudge.api.submission.dto.LikeVO;

public interface CustomLikeRepository {
    Page<LikeVO> getAllLike(String email, GetAllLikeRequest req);
}
