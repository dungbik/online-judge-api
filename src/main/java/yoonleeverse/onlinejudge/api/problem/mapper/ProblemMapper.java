package yoonleeverse.onlinejudge.api.problem.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import yoonleeverse.onlinejudge.api.problem.dto.GetProblemResponse;
import yoonleeverse.onlinejudge.api.problem.entity.Problem;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface ProblemMapper {
    GetProblemResponse toDto(Problem source);
}
