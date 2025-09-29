package com.hs.lab1.serviceTests;

import com.hs.lab1.entity.Event;
import com.hs.lab1.entity.User;
import com.hs.lab1.exceptions.EventConflictException;
import com.hs.lab1.exceptions.EventNotFoundException;
import com.hs.lab1.exceptions.UserNotFoundException;
import com.hs.lab1.repository.EventRepository;
import com.hs.lab1.repository.UserRepository;
import com.hs.lab1.service.EventService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class EventServiceTest {

    @Mock
    private EventRepository eventRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private EventService eventService;

    @Test
    void addEvent_userNotFound_throwsException() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(UserNotFoundException.class, () ->
                eventService.addEvent("Test", "aaa", any(), any(), any(), 1L));
    }

    @Test
    void addEvent_success() {
        User user = new User();
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(eventRepository.existsByOwnerAndDateAndTimeOverlap(any(), any(), any(), any())).thenReturn(false);

        Event savedEvent = new Event();
        when(eventRepository.save(any(Event.class))).thenReturn(savedEvent);

        Event result = eventService.addEvent("Test", "aaa", LocalDate.now(),
                LocalTime.NOON, LocalTime.NOON.plusHours(1), 1L);

        assertEquals(savedEvent, result);
    }


    @Test
    void addEvent_existsByOwnerAndDateAndTimeOverlap_throwsException() {
        User user = new User();
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        when(eventRepository.existsByOwnerAndDateAndTimeOverlap(user, any(), any(), any())).thenReturn(true);
        assertThrows(EventConflictException.class, () ->
                eventService.addEvent("Test", "aaa", LocalDate.now(), LocalTime.NOON, LocalTime.NOON.plusHours(1), 1L));
    }

    @Test
    void getEventById_existsById_throwsException() {
        Long id = 1L;
        when(eventRepository.findById(id)).thenReturn(Optional.empty());
        assertThrows(EventNotFoundException.class, () ->
                eventService.getEventById(id));
    }

    @Test
    void deleteEventById_existsById_throwsException() {
        Long id = 1L;
        when(eventRepository.findById(id)).thenReturn(Optional.empty());
        assertThrows(EventNotFoundException.class, () ->
                eventService.deleteEventById(id));
    }

    @Test
    void addEvent_invalidTime_throwsException() {
        User user = new User();
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        assertThrows(IllegalArgumentException.class, () ->
                eventService.addEvent("Test", "aaa", LocalDate.now().minusDays(1),
                        LocalTime.NOON, LocalTime.NOON.plusHours(1), 1L));

        assertThrows(IllegalArgumentException.class, () ->
                eventService.addEvent("Test", "aaa", LocalDate.now(),
                        LocalTime.NOON.plusHours(1), LocalTime.NOON, 1L));
    }
}
