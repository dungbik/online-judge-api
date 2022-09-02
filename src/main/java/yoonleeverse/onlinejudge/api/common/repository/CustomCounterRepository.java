package yoonleeverse.onlinejudge.api.common.repository;

public interface CustomCounterRepository {
    long getNextSequence(String collectionName);
}
