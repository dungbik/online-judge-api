package yoonleeverse.onlinejudge.api.problem.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.springframework.data.domain.Page;
import yoonleeverse.onlinejudge.api.common.dto.PagingResponse;
import yoonleeverse.onlinejudge.api.problem.dto.GetProblemResponse;
import yoonleeverse.onlinejudge.api.problem.entity.Problem;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface ProblemMapper {
    GetProblemResponse toDto(Problem source);
    List<GetProblemResponse> toDtoList(List<Problem> source);

    @Mapping(target = "currentPages", expression = "java(source.getNumber() + 1)")
    PagingResponse toPageDto(Page source);
}
