package yoonleeverse.onlinejudge.api.common.repository;

public interface CustomCounterRepository {

    /**
     * get Auto Increment Seq By CollectionName (starting from 1)
     * @param collectionName
     * @return Seq
     */
    long getNextSequence(String collectionName);
}
