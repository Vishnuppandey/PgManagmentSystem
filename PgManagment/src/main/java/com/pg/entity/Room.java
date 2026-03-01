package com.pg.entity;

import com.pg.enums.RoomStatus;
import com.pg.enums.SharingType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "rooms",
        indexes = {
                @Index(name = "idx_room_number", columnList = "roomNumber"),
                @Index(name = "idx_room_status", columnList = "status")
        })
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Room {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Integer roomNumber;

    @Column(nullable = false)
    private Integer floor;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SharingType sharingType;   // SINGLE, DOUBLE, TRIPLE

    @Column(nullable = false)
    private Integer capacity;

    @Column(nullable = false)
    private Integer availableBeds;

    @Column(nullable = false)
    private Double rent;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RoomStatus status;   // AVAILABLE, FULL, MAINTENANCE

    @Column(nullable = false)
    private Boolean active = true;

    @Column(nullable = false)
    private Long ownerId;   // from User Service
}
