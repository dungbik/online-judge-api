package yoonleeverse.onlinejudge.api.common.service;

import yoonleeverse.onlinejudge.api.common.dto.EmailMessage;

public interface EmailService {

    void sendMessage(EmailMessage emailMessage);
}
