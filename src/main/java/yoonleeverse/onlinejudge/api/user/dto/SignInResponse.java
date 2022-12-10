package yoonleeverse.onlinejudge.api.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import yoonleeverse.onlinejudge.api.common.dto.APIResponse;
import yoonleeverse.onlinejudge.api.user.dto.data.OAuthLinkDTO;
import yoonleeverse.onlinejudge.api.user.dto.data.UserDTO;
import yoonleeverse.onlinejudge.api.user.entity.UserEntity;

import java.util.List;
import java.util.stream.Collectors;

@Data
@EqualsAndHashCode(callSuper=false)
public class SignInResponse extends APIResponse {

    @Schema(example = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiI2MmRkN2U2NTY0MWYzMDI3OTZjNTMyZDciLCJpYXQiOjE2NTkyNTkxMjMsImV4cCI6MTY2MDEyMzEyM30.cyTvifMKLqh4UeMAgl9E_QY5MBtJWeSf-XgdcIFdW8OLq7YFygcdE7dXFFX_KhjZL4oApjn5iYAeh0KUQKEgNA", description = "새로 발급된 accessToken")
    private String accessToken;

    @Schema(description = "유저 정보")
    private UserDTO user;

    public static SignInResponse ofSuccess(String accessToken, UserEntity userEntity) {
        List<OAuthLinkDTO> oAuthLinkDTOS = userEntity.getLinks().stream()
                .map((link -> new OAuthLinkDTO(link.getProvider(), link.getEmail(), link.getAvatarUrl())))
                .collect(Collectors.toList());
        UserDTO userDTO = new UserDTO(userEntity.getName(), userEntity.getEmail(), userEntity.getAvatarUrl(), oAuthLinkDTOS, userEntity.getRoles());

        SignInResponse response = new SignInResponse();
        response.setSuccess(true);
        response.setAccessToken(accessToken);
        response.setUser(userDTO);

        return response;
    }
}
