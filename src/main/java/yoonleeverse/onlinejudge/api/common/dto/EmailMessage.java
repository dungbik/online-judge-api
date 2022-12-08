package yoonleeverse.onlinejudge.api.common.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmailMessage {
    private String to;
    private String subject;
    private String message;

    public static EmailMessage of(String to, String subject, String message) {
        return new EmailMessage(to, subject, message);
    }
}
