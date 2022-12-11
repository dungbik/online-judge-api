package yoonleeverse.onlinejudge.api.submission.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.*;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.support.PageableExecutionUtils;
import yoonleeverse.onlinejudge.api.submission.dto.GetAllLikeRequest;
import yoonleeverse.onlinejudge.api.submission.dto.LikeVO;
import yoonleeverse.onlinejudge.api.submission.entity.Like;
import yoonleeverse.onlinejudge.util.NumberUtil;

import java.util.List;

import static org.springframework.data.mongodb.core.aggregation.ConvertOperators.ToObjectId.toObjectId;
import static yoonleeverse.onlinejudge.api.common.constant.Constants.LIKE_MAX_SIZE;

@RequiredArgsConstructor
public class CustomLikeRepositoryImpl implements CustomLikeRepository {

    private final MongoTemplate mongoTemplate;

    @Override
    public Page<LikeVO> getAllLike(String email, GetAllLikeRequest req) {

        int page = NumberUtil.toPage(req.getPage());
        Criteria criteria = Criteria.where("userId").is(email);

        MatchOperation match = Aggregation.match(criteria);
        SortOperation sort = Aggregation.sort(Sort.by(Sort.Direction.DESC, "_id"));
        SkipOperation skip = Aggregation.skip(page * LIKE_MAX_SIZE);
        LimitOperation limit = Aggregation.limit(LIKE_MAX_SIZE);
        AddFieldsOperation addFields = Aggregation.addFields()
                .addField("submission_id").withValue(toObjectId("$submissionId"))
                .build();
        LookupOperation lookup = Aggregation.lookup("submissions", "submission_id", "_id", "submission");
        UnwindOperation unwind = Aggregation.unwind("submission", Boolean.TRUE);

        Aggregation agg = Aggregation.newAggregation(match, sort, skip, limit, addFields, lookup, unwind);

        AggregationResults<LikeVO> aggregationResults = mongoTemplate.aggregate(agg, "likes", LikeVO.class);
        List<LikeVO> likes = aggregationResults.getMappedResults();

        Pageable pageable = PageRequest.of(page, LIKE_MAX_SIZE);

        return PageableExecutionUtils.getPage(
                likes,
                pageable,
                () -> mongoTemplate.count(new Query(criteria), Like.class)
        );
    }
}
