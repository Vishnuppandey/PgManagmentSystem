package com.pg.entity;

import jakarta.persistence.*;

@Entity
@Table(name="room")
public class RoomDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int floor;
    private String sharingType;

}
