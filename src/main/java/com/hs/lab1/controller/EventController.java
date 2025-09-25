package com.hs.lab1.controller;

import com.hs.lab1.dto.EventDto;
import com.hs.lab1.entity.Event;
import com.hs.lab1.mapper.EventMapper;
import com.hs.lab1.requests.CreateEventRequest;
import com.hs.lab1.service.EventService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/event")
@RequiredArgsConstructor
public class EventController {
    private final EventService eventService;
    private final EventMapper eventMapper;

    @PostMapping
    public ResponseEntity<EventDto> addEvent(@Valid @RequestBody CreateEventRequest request) {
        Event event = eventService.addEvent(
                request.name(),
                request.description(),
                request.date(),
                request.startTime(),
                request.endTime(),
                request.ownerId());
        return ResponseEntity.ok(eventMapper.toEventDto(event));
    }

    @GetMapping
    public ResponseEntity<List<EventDto>> getAllEvents() {
        List<Event> events = eventService.getAllEvents();
        return ResponseEntity.ok(eventMapper.toEventDtoList(events));
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<EventDto> getEventById(@PathVariable @Min(1) Long id) {
        Event event = eventService.getEventById(id);
        return ResponseEntity.ok(eventMapper.toEventDto(event));
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<Void> deleteEventById(@PathVariable @Min(1) Long id) {
        eventService.deleteEventById(id);
        return ResponseEntity.ok().build();
    }
}
