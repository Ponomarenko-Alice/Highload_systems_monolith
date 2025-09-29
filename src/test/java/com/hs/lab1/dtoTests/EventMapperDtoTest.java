package com.hs.lab1.dtoTests;

import com.hs.lab1.dto.EventDto;
import com.hs.lab1.entity.Event;
import com.hs.lab1.entity.User;
import com.hs.lab1.mapper.EventMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class EventMapperDtoTest {

    private EventMapper eventMapper;

    @BeforeEach
    void setUp() {
        eventMapper = Mappers.getMapper(EventMapper.class);
    }

    @Test
    void testToEventDto() {
        User owner = new User();
        owner.setId(100L);

        Event event = new Event();
        event.setId(1L);
        event.setName("Test Event");
        event.setDescription("Description");
        event.setDate(LocalDate.of(2025, 10, 1));
        event.setStartTime(LocalTime.of(12, 0));
        event.setEndTime(LocalTime.of(14, 0));
        event.setOwner(owner);

        EventDto dto = eventMapper.toEventDto(event);

        assertNotNull(dto);
        assertEquals(event.getId(), dto.id());
        assertEquals(event.getName(), dto.name());
        assertEquals(event.getDescription(), dto.description());
        assertEquals(event.getDate(), dto.date());
        assertEquals(event.getStartTime(), dto.startTime());
        assertEquals(event.getEndTime(), dto.endTime());
        assertEquals(owner.getId(), dto.ownerId());
    }

    @Test
    void testToEventDtoList() {
        User owner = new User();
        owner.setId(200L);

        Event event1 = new Event();
        event1.setId(1L);
        event1.setName("Event1");
        event1.setOwner(owner);

        Event event2 = new Event();
        event2.setId(2L);
        event2.setName("Event2");
        event2.setOwner(owner);

        List<EventDto> dtoList = eventMapper.toEventDtoList(List.of(event1, event2));

        assertNotNull(dtoList);
        assertEquals(2, dtoList.size());
        assertEquals("Event1", dtoList.get(0).name());
        assertEquals("Event2", dtoList.get(1).name());
        assertEquals(owner.getId(), dtoList.get(0).ownerId());
    }
}

