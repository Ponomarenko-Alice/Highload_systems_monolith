package com.hs.lab1.repository;

import com.hs.lab1.entity.Event;
import com.hs.lab1.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.time.LocalTime;

public interface EventRepository extends JpaRepository<Event, Long> {

    @Query("""
    SELECT CASE WHEN COUNT(e) > 0 THEN true ELSE false END
    FROM Event e
    WHERE e.owner = :owner
      AND e.date = :date
      AND e.startTime < :endTime
      AND e.endTime > :startTime
    """)
    boolean existsByOwnerAndDateAndTimeOverlap(User owner, LocalDate date, LocalTime startTime, LocalTime endTime);
}
