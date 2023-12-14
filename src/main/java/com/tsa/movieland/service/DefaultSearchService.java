package com.tsa.movieland.service;

import jakarta.jms.*;
import lombok.RequiredArgsConstructor;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DefaultSearchService implements SearchService {
    private final JmsTemplate jmsTemplate;

    @Override
    public List<Integer> search(String title) {
        try {
            Message receive = jmsTemplate.sendAndReceive(session -> {
                Destination tempDest = session.createTemporaryQueue();
                String correlationId = UUID.randomUUID().toString();

                TextMessage textMessage = session.createTextMessage(title);
                textMessage.setJMSReplyTo(tempDest);
                textMessage.setJMSCorrelationID(correlationId);
                return textMessage;
            });

            TextMessage message = (TextMessage) receive;
            List<Integer> list = Arrays.stream(message.getText().split(";"))
                    .map(Integer::valueOf).toList();
            System.out.println(message.getText());

            return list;
        } catch (JMSException e) {
            throw new RuntimeException(e);
        }
    }
}
