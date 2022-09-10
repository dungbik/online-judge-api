package yoonleeverse.onlinejudge.api.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
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
@AllArgsConstructor
public class CurrentUserResponse extends APIResponse {

    @Schema(description = "유저 정보")
    private UserDTO user;

    public static CurrentUserResponse ofSuccess(UserEntity userEntity) {
        List<OAuthLinkDTO> oAuthLinkDTOS = userEntity.getLinks().stream()
                    .map((link -> new OAuthLinkDTO(link.getProvider(), link.getEmail(), link.getAvatarUrl())))
                    .collect(Collectors.toList());
        UserDTO userDTO = new UserDTO(userEntity.getName(), userEntity.getId(), userEntity.getAvatarUrl(), oAuthLinkDTOS);

        CurrentUserResponse response = new CurrentUserResponse(userDTO);
        response.setSuccess(true);

        return response;
    }
}
