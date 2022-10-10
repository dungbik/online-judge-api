package yoonleeverse.onlinejudge.api.submission.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import yoonleeverse.onlinejudge.api.submission.dto.SubmitProblemRequest;
import yoonleeverse.onlinejudge.api.submission.entity.JudgeStatus;
import yoonleeverse.onlinejudge.api.submission.entity.Submission;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, imports = JudgeStatus.class)
public interface SubmissionMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "userId", ignore = true)
    @Mapping(target = "status", expression = "java(JudgeStatus.PENDING)")
    Submission toEntity(SubmitProblemRequest source);
}
