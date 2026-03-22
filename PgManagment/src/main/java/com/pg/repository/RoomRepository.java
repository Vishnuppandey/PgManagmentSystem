package com.pg.repository;

import com.pg.entity.Room;
import com.pg.enums.RoomStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import jakarta.persistence.LockModeType;
import java.util.List;

@Repository
public interface RoomRepository extends JpaRepository<Room,Long> {

    List<Room> findByOwnerId(Long id);
    List<Room> findByStatusAndActiveTrueAndAvailableBedsGreaterThan(
            RoomStatus status,
            Integer beds
    );
    List<Room> findByStatusAndActiveTrue(RoomStatus status);
    
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT r FROM Room r WHERE r.id = :id")
    Room findByIdForUpdate(Long id);
    boolean existsByOwnerIdAndRoomNumber(Long ownerId, Integer roomNumber);
}
