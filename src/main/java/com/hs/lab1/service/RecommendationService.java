package com.hs.lab1.service;

import com.hs.lab1.dto.TimeInterval;
import com.hs.lab1.dto.TimeSlot;
import com.hs.lab1.entity.Event;
import com.hs.lab1.entity.GroupEvent;
import com.hs.lab1.entity.User;
import com.hs.lab1.enums.GroupEventStatus;
import com.hs.lab1.exceptions.EventNotFoundException;
import com.hs.lab1.exceptions.NoAvailableSlotsException;
import com.hs.lab1.repository.EventRepository;
import com.hs.lab1.repository.GroupEventRepository;
import com.hs.lab1.util.SlotCalculator;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class RecommendationService {
    private final GroupEventRepository groupEventRepository;
    private final EventRepository eventRepository;

    public RecommendationService(GroupEventRepository groupEventRepository, EventRepository eventRepository) {
        this.groupEventRepository = groupEventRepository;
        this.eventRepository = eventRepository;
    }

    @Transactional
    public List<TimeSlot> recommendSlots(
            LocalDate periodStart,
            LocalDate periodEnd,
            Duration duration,
            Long groupEventId
    ) {
        Optional<GroupEvent> optGroupEvent = groupEventRepository.findById(groupEventId);
        if (optGroupEvent.isEmpty()) {
            throw new EventNotFoundException("GroupEvent not found: " + groupEventId);
        }

        List<Long> participantIds = optGroupEvent.get().getParticipants().stream().map(User::getId).collect(Collectors.toList());
        List<Event> events = eventRepository.findBusyEventsForUsersBetweenDates(
                participantIds, periodStart, periodEnd
        );

        List<TimeInterval> busyIntervals = events.stream()
                .map(e -> new TimeInterval(e.getDate(), e.getStartTime(), e.getEndTime()))
                .toList();

        List<TimeSlot> freeSlots = SlotCalculator.findCommonFreeSlots(
                periodStart, periodEnd, busyIntervals, duration
        );

        if (freeSlots.isEmpty()) {
            throw new NoAvailableSlotsException("No free slots available");
        }
        return freeSlots.stream().limit(5).toList();
    }

    @Transactional
    public GroupEvent bookSlot(Long groupEventId, TimeSlot chosenSlot) {

        Optional<GroupEvent> optGroupEvent = groupEventRepository.findById(groupEventId);
        if (optGroupEvent.isEmpty()) {
            throw new EventNotFoundException("GroupEvent not found: " + groupEventId);
        }
        GroupEvent groupEvent = optGroupEvent.get();

        groupEvent.setDate(chosenSlot.date());
        groupEvent.setStartTime(chosenSlot.start());
        groupEvent.setEndTime(chosenSlot.end());
        groupEvent.setStatus(GroupEventStatus.CONFIRMED);

        return groupEventRepository.save(groupEvent);
    }

}
