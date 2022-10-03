package yoonleeverse.onlinejudge.api.problem.entity;

import lombok.Getter;

@Getter
public class SubmissionHistory {
    private long totalCount;
    private long successCount;

    public void addTotalCount() {
        this.totalCount += 1;
    }

    public void addSuccessCount() {
        this.successCount += 1;
    }

    public long getFailCount() {
        return totalCount - successCount;
    }
}
