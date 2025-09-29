package com.hs.lab1.service;

import com.hs.lab1.entity.GroupEvent;
import com.hs.lab1.entity.User;
import com.hs.lab1.enums.GroupEventStatus;
import com.hs.lab1.exceptions.EventNotFoundException;
import com.hs.lab1.exceptions.UserNotFoundException;
import com.hs.lab1.mapper.GroupEventMapper;
import com.hs.lab1.repository.GroupEventRepository;
import com.hs.lab1.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GroupEventService {
    private final GroupEventRepository groupEventRepository;
    private final GroupEventMapper groupEventMapper;
    private final UserRepository userRepository;

    public List<GroupEvent> getAllGroupEvents() {
        return groupEventRepository.findAll();
    }

    @Transactional
    public GroupEvent addGroupEvent(String name, String description, LocalDate date, LocalTime startTime, LocalTime endTime, List<Long> participantsIds, Long ownerId, GroupEventStatus status) {
        Optional<User> owner = userRepository.findById(ownerId);
        if (owner.isEmpty()) throw new UserNotFoundException("User with id = " + ownerId + " not found");

        List<Optional<User>> eventParticipants = participantsIds.stream().map(userRepository::findById).toList();

        List<User> foundUsers = userRepository.findAllById(participantsIds);
        Set<Long> foundIds = foundUsers.stream().map(User::getId).collect(Collectors.toSet());
        List<Long> notFoundUserIds = participantsIds.stream().filter(id -> !foundIds.contains(id)).toList();
        if (!notFoundUserIds.isEmpty()) {
            throw new UserNotFoundException("Users not found: " + notFoundUserIds);
        }

        GroupEvent groupEvent = new GroupEvent();
        groupEvent.setName(name);
        groupEvent.setDescription(description);
        groupEvent.setDate(date);
        groupEvent.setStartTime(startTime);
        groupEvent.setEndTime(endTime);
        groupEvent.setParticipants(foundUsers);
        groupEvent.setOwner(owner.get());
        groupEvent.setStatus(status);

        return groupEventRepository.save(groupEvent);
    }

    public GroupEvent getGroupEventById(Long id) {
        Optional<GroupEvent> groupEvent = groupEventRepository.findById(id);
        if (groupEvent.isEmpty()) throw new EventNotFoundException("Group event with id = " + id + " not found");
        return groupEvent.get();
    }

    public void deleteGroupEventById(Long id) {
        Optional<GroupEvent> groupEvent = groupEventRepository.findById(id);
        if (groupEvent.isEmpty()) throw new EventNotFoundException("Group event with id = " + id + " not found");
        groupEventRepository.deleteById(id);
    }

}
