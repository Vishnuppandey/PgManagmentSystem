package com.pg.booking.kafka;

import com.pg.entity.Room;
import com.pg.enums.RoomStatus;
import com.pg.repository.RoomRepository;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class BookingEventConsumer {

    private final RoomRepository repository;

    public BookingEventConsumer(RoomRepository repository) {
        this.repository = repository;
    }

    @KafkaListener(topics = "booking-events", groupId = "room-service-group")
    public void consumeBookingEvent(BookingEvent event){

        Room room = repository.findById(event.getRoomId())
                .orElseThrow(() -> new RuntimeException("Room not found"));

        switch(event.getEventType()){

            case "BOOKING_CREATED":

                room.setAvailableBeds(room.getAvailableBeds() - 1);

                if(room.getAvailableBeds() == 0){
                    room.setStatus(RoomStatus.FULL);
                }

                break;

            case "BOOKING_CANCELLED":

            case "TENANT_LEFT":

                room.setAvailableBeds(room.getAvailableBeds() + 1);
                room.setStatus(RoomStatus.AVAILABLE);

                break;
        }

        repository.save(room);
    }
}
