package yoonleeverse.onlinejudge.api.submission.listener;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import yoonleeverse.onlinejudge.api.submission.dto.CompleteMessage;
import yoonleeverse.onlinejudge.api.submission.service.JudgeService;
import yoonleeverse.onlinejudge.config.RabbitMQConfig;

@Component
@Slf4j
@RequiredArgsConstructor
public class CompleteListener {

    private final JudgeService judgeService;

    @RabbitListener(queues = RabbitMQConfig.COMPLETE_QUEUE_NAME, containerFactory = "containerFactory")
    public void onMessage(CompleteMessage completeMessage) {
        log.debug("complete thread-{} {}", Thread.currentThread().getName().split("-")[1], completeMessage.getSubmissionId());
        judgeService.completeJudge(completeMessage);
    }
}
