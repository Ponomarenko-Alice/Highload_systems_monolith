package com.hs.lab1.controller;

import com.hs.lab1.dto.GroupEventDto;
import com.hs.lab1.entity.GroupEvent;
import com.hs.lab1.mapper.GroupEventMapper;
import com.hs.lab1.mapper.UserMapper;
import com.hs.lab1.requests.CreateGroupEventRequest;
import com.hs.lab1.service.GroupEventService;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/groupEvent")
@RequiredArgsConstructor
public class GroupEventController {
    private final GroupEventService groupEventService;
    private final GroupEventMapper groupEventMapper;
    private final UserMapper userMapper;

    @PostMapping
    public ResponseEntity<GroupEventDto> addGroupEvent(@RequestBody CreateGroupEventRequest request) {
        GroupEvent groupEvent = groupEventService.addGroupEvent(
                request.name(),
                request.description(),
                request.date(),
                request.startTime(),
                request.endTime(),
                request.participantIds(),
                request.ownerId(),
                request.status()
        );
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(groupEventMapper.toGroupEventDto(groupEvent));
    }

    @GetMapping
    public ResponseEntity<List<GroupEventDto>> getAllGroupEvents() {
        List<GroupEvent> groupEvents = groupEventService.getAllGroupEvents();
        return ResponseEntity.ok(groupEventMapper.toGroupEventDtoList(groupEvents));
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<GroupEventDto> getGroupEventById(@PathVariable @Min(1) Long id) {
        GroupEvent groupEvent = groupEventService.getGroupEventById(id);
        return ResponseEntity.ok(groupEventMapper.toGroupEventDto(groupEvent));
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<Void> deleteGroupEventById(@PathVariable @Min(1) Long id) {
        groupEventService.deleteGroupEventById(id);
        return ResponseEntity.ok().build();
    }


}
