package yoonleeverse.onlinejudge.api.submission.service;

import yoonleeverse.onlinejudge.api.submission.dto.CompleteMessage;
import yoonleeverse.onlinejudge.api.submission.entity.Submission;

public interface JudgeService {
     void judge(Submission submission);

     void completeJudge(CompleteMessage completeMessage);
}
