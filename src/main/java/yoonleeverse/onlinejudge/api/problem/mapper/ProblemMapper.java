package yoonleeverse.onlinejudge.api.problem.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.springframework.data.domain.Page;
import yoonleeverse.onlinejudge.api.common.dto.PagingResponse;
import yoonleeverse.onlinejudge.api.problem.dto.AddProblemRequest;
import yoonleeverse.onlinejudge.api.problem.dto.GetProblemResponse;
import yoonleeverse.onlinejudge.api.problem.entity.Problem;
import yoonleeverse.onlinejudge.api.problem.entity.SubmissionHistory;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, imports = SubmissionHistory.class)
public interface ProblemMapper {
    GetProblemResponse toDto(Problem source);

    List<GetProblemResponse> toDtoList(List<Problem> source);

    @Mapping(target = "currentPages", expression = "java(source.getNumber() + 1)")
    PagingResponse toPageDto(Page source);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "testCaseExamples", ignore = true)
    @Mapping(target = "testCases", ignore = true)
    @Mapping(target = "tags", ignore = true)
    @Mapping(target = "submissionHistory", expression = "java(new SubmissionHistory())")
    @Mapping(target = "userId", ignore = true)
    Problem toEntity(AddProblemRequest source);
}
