package com.hs.lab1.service;

import com.hs.lab1.entity.Event;
import com.hs.lab1.entity.User;
import com.hs.lab1.exceptions.EventConflictException;
import com.hs.lab1.exceptions.EventNotFoundException;
import com.hs.lab1.exceptions.UserNotFoundException;
import com.hs.lab1.repository.EventRepository;
import com.hs.lab1.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EventService {
    private final EventRepository eventRepository;
    private final UserRepository userRepository;

    public List<Event> getAllEvents() {
        return eventRepository.findAll();
    }

    @Transactional
    public Event addEvent(String name, String description, LocalDate date, LocalTime startTime, LocalTime endTime, Long ownerId) {
        if (endTime.isBefore(startTime) || date.isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("Invalid event time");
        }

        Optional<User> user = userRepository.findById(ownerId);
        if (user.isEmpty()) throw new UserNotFoundException("User with id = " + ownerId + " not found");

        boolean conflict = eventRepository.existsByOwnerAndDateAndTimeOverlap(
                user.get(), date, startTime, endTime);
        if (conflict) {
            throw new EventConflictException("User already has an event at this time");
        }

        Event event = new Event();

        event.setName(name);
        event.setDescription(description);
        event.setDate(date);
        event.setStartTime(startTime);
        event.setEndTime(endTime);
        event.setOwner(user.get());

        return eventRepository.save(event);
    }

    public Event getEventById(Long id) {
        Optional<Event> event = eventRepository.findById(id);
        if (event.isEmpty()) throw new EventNotFoundException("Event with id = " + id + " not found");
        return event.get();
    }

    public void deleteEventById(Long id) {
        Optional<Event> event = eventRepository.findById(id);
        if (event.isEmpty()) throw new EventNotFoundException("Event with id = " + id + " not found");
        eventRepository.deleteById(id);
    }
}
