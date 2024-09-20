package se.sowl.yesterdaymembership.rabbitmq;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import se.sowl.yesterdaymembership.service.MembershipService;

@Component
@RequiredArgsConstructor
@Slf4j
public class NewsRabbitConsumer {
    private final MembershipService membershipService;

    @RabbitListener(queues = "add-membership-queue")
    public void receiveNewsUpdateMessage(String message) {
        try {
            if ("UPDATE_NEWS".equals(message)) {
//                membershipService.apply();
                log.info("메세지 처리 완료: UPDATE_NEWS");
            } else {
                log.warn("해석할 수 없는 메세지를 받았어요.: {}", message);
            }
        } catch (Exception e) {
            log.error("뉴스 갱신 메세지 큐 처리 도중 문제가 생겼어요.", e);
            // TODO: retry queue?
        }
    }
}
