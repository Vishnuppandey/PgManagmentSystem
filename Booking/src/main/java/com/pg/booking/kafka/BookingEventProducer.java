package com.pg.booking.kafka;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class BookingEventProducer {

    private final KafkaTemplate<String, BookingEvent> kafkaTemplate;

    public BookingEventProducer(KafkaTemplate<String, BookingEvent> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void publishBookingEvent(BookingEvent event) {
        kafkaTemplate.send("booking-events", event);
    }
}
