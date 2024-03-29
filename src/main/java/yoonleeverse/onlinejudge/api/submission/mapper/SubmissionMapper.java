package yoonleeverse.onlinejudge.api.submission.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.springframework.data.domain.Page;
import yoonleeverse.onlinejudge.api.common.dto.PagingResponse;
import yoonleeverse.onlinejudge.api.submission.dto.RunResult;
import yoonleeverse.onlinejudge.api.submission.dto.SubmissionVO;
import yoonleeverse.onlinejudge.api.submission.dto.SubmitProblemRequest;
import yoonleeverse.onlinejudge.api.submission.entity.JudgeResult;
import yoonleeverse.onlinejudge.api.submission.entity.JudgeStatus;
import yoonleeverse.onlinejudge.api.submission.entity.Submission;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, imports = JudgeStatus.class)
public interface SubmissionMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "userId", ignore = true)
    @Mapping(target = "memory", ignore = true)
    @Mapping(target = "realTime", ignore = true)
    @Mapping(target = "resultList", ignore = true)
    @Mapping(target = "status", expression = "java(JudgeStatus.PENDING)")
    @Mapping(target = "codeLength", expression = "java(source.getCode().length())")
    @Mapping(target = "isJudge", source = "judge")
    @Mapping(target = "like", expression = "java(0)")
    Submission toEntity(SubmitProblemRequest source);

    @Mapping(target = "liked", ignore = true)
    @Mapping(target = "comments", ignore = true)
    SubmissionVO toDto(Submission source);

    List<SubmissionVO> toDtoList(List<Submission> source);

    @Mapping(target = "currentPages", expression = "java(source.getNumber() + 1)")
    PagingResponse toPageDto(Page source);

    @Mapping(target = "correct", ignore = true)
    JudgeResult toResult(RunResult source);

    List<JudgeResult> toResultList(List<RunResult> source);
}
