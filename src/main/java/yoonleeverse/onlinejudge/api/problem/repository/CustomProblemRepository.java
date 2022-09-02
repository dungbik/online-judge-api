package yoonleeverse.onlinejudge.api.problem.repository;

import yoonleeverse.onlinejudge.api.problem.entity.ProblemEntity;

public interface CustomProblemRepository {

    ProblemEntity saveWithId(ProblemEntity problem);
}
