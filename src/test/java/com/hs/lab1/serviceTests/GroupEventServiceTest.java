package com.hs.lab1.serviceTests;

import com.hs.lab1.entity.GroupEvent;
import com.hs.lab1.entity.User;
import com.hs.lab1.enums.GroupEventStatus;
import com.hs.lab1.exceptions.EventNotFoundException;
import com.hs.lab1.exceptions.UserNotFoundException;
import com.hs.lab1.mapper.GroupEventMapper;
import com.hs.lab1.repository.GroupEventRepository;
import com.hs.lab1.repository.UserRepository;
import com.hs.lab1.service.GroupEventService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class GroupEventServiceTest {

    @Mock
    private GroupEventRepository groupEventRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private GroupEventMapper groupEventMapper;

    @InjectMocks
    private GroupEventService groupEventService;

    @Test
    void addGroupEvent_ownerNotFound_throwsException() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () ->
                groupEventService.addGroupEvent(
                        "Name", "Desc", LocalDate.now(),
                        LocalTime.NOON, LocalTime.NOON.plusHours(1),
                        List.of(2L, 3L), 1L, GroupEventStatus.PENDING
                )
        );
    }

    @Test
    void addGroupEvent_participantNotFound_throwsException() {
        User owner = new User();
        owner.setId(1L);
        when(userRepository.findById(1L)).thenReturn(Optional.of(owner));

        when(userRepository.findAllById(List.of(2L, 3L))).thenReturn(List.of(new User())); // только один найден

        assertThrows(UserNotFoundException.class, () ->
                groupEventService.addGroupEvent(
                        "Name", "Desc", LocalDate.now(),
                        LocalTime.NOON, LocalTime.NOON.plusHours(1),
                        List.of(2L, 3L), 1L, GroupEventStatus.PENDING
                )
        );
    }

    @Test
    void addGroupEvent_success() {
        User owner = new User();
        owner.setId(1L);

        User participant1 = new User();
        participant1.setId(2L);
        User participant2 = new User();
        participant2.setId(3L);

        when(userRepository.findById(1L)).thenReturn(Optional.of(owner));
        when(userRepository.findAllById(List.of(2L, 3L))).thenReturn(List.of(participant1, participant2));

        GroupEvent savedEvent = new GroupEvent();
        savedEvent.setId(10L);
        when(groupEventRepository.save(any(GroupEvent.class))).thenReturn(savedEvent);

        GroupEvent result = groupEventService.addGroupEvent(
                "Name", "Desc", LocalDate.now(),
                LocalTime.NOON, LocalTime.NOON.plusHours(1),
                List.of(2L, 3L), 1L, GroupEventStatus.CONFIRMED
        );

        assertEquals(savedEvent.getId(), result.getId());
        verify(groupEventRepository, times(1)).save(any(GroupEvent.class));
    }

    @Test
    void getGroupEventById_found() {
        GroupEvent event = new GroupEvent();
        event.setId(5L);
        when(groupEventRepository.findById(5L)).thenReturn(Optional.of(event));

        GroupEvent result = groupEventService.getGroupEventById(5L);
        assertEquals(event, result);
    }

    @Test
    void getGroupEventById_notFound_throwsException() {
        when(groupEventRepository.findById(5L)).thenReturn(Optional.empty());
        assertThrows(EventNotFoundException.class, () -> groupEventService.getGroupEventById(5L));
    }

    @Test
    void deleteGroupEventById_found() {
        GroupEvent event = new GroupEvent();
        event.setId(5L);
        when(groupEventRepository.findById(5L)).thenReturn(Optional.of(event));

        groupEventService.deleteGroupEventById(5L);
        verify(groupEventRepository, times(1)).deleteById(5L);
    }

    @Test
    void deleteGroupEventById_notFound_throwsException() {
        when(groupEventRepository.findById(5L)).thenReturn(Optional.empty());
        assertThrows(EventNotFoundException.class, () -> groupEventService.deleteGroupEventById(5L));
    }
}
