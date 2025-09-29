package com.hs.lab1.repository;

import com.hs.lab1.entity.Event;
import com.hs.lab1.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

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

    @Query("""
        select e
        from Event e
        where e.owner.id in :userIds
          and e.date between :from and :to
        """)
    List<Event> findBusyEventsForUsersBetweenDates(
            @Param("userIds") List<Long> userIds,
            @Param("from") LocalDate from,
            @Param("to") LocalDate to
    );

}
