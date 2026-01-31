package com.hscp.mail.service;

import com.hscp.mail.event.user.UserCreatedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class MailSender {

    private final IdempotencyStore idempotencyStore;
    public void sendWelcomeMail(UserCreatedEvent event) {
        String idempotencyKey = "WELCOME_MAIL_" + event.getEmail();
        if(idempotencyStore.isProcessed(idempotencyKey)){
            log.info("🔁 Mail already sent for userId={}, skipping",
                    event.getUserId());
            return;
        }

        log.info("📧 Sending welcome mail to {}", event.getEmail());

        // SMTP call

        idempotencyStore.markProcessed(idempotencyKey);
        log.info("✅ Mail marked as sent for userId={}", event.getEmail());
    }
}