package com.hs.lab1.controller;

import com.hs.lab1.dto.GroupEventDto;
import com.hs.lab1.entity.GroupEvent;
import com.hs.lab1.mapper.GroupEventMapper;
import com.hs.lab1.mapper.UserMapper;
import com.hs.lab1.requests.CreateGroupEventRequest;
import com.hs.lab1.service.GroupEventService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
        return ResponseEntity.ok(groupEventMapper.toGroupEventDto(groupEvent));
    }
}
