package yoonleeverse.onlinejudge.api.user.repository;

import org.springframework.data.repository.CrudRepository;
import yoonleeverse.onlinejudge.api.user.entity.OAuthLink;

public interface OAuthLinkRedisRepository extends CrudRepository<OAuthLink, String> {
}
