package com.hs.lab1.controllerTests;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.hs.lab1.controller.EventController;
import com.hs.lab1.dto.EventDto;
import com.hs.lab1.entity.Event;
import com.hs.lab1.mapper.EventMapper;
import com.hs.lab1.requests.CreateEventRequest;
import com.hs.lab1.service.EventService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class EventControllerTest {

    private MockMvc mockMvc;

    @Mock
    private EventService eventService;

    @Mock
    private EventMapper eventMapper;

    @InjectMocks
    private EventController eventController;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(eventController).build();
        objectMapper.registerModule(new JavaTimeModule());
    }

    @Test
    void addEvent_success() throws Exception {
        CreateEventRequest request = new CreateEventRequest(
                "Test", "Desc", LocalDate.now(), LocalTime.NOON, LocalTime.NOON.plusHours(1), 1L
        );

        Event event = new Event();
        EventDto dto = new EventDto(1L, "Test", "Desc", LocalDate.now(), LocalTime.NOON, LocalTime.NOON.plusHours(1), 1L);

        when(eventService.addEvent(anyString(), anyString(), any(), any(), any(), anyLong())).thenReturn(event);
        when(eventMapper.toEventDto(event)).thenReturn(dto);

        mockMvc.perform(post("/api/v1/event")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1));

        verify(eventService, times(1)).addEvent(anyString(), anyString(), any(), any(), any(), anyLong());
    }

    @Test
    void getAllEvents_success() throws Exception {
        Event event = new Event();
        EventDto dto = new EventDto(1L, "Test", "Desc", LocalDate.now(), LocalTime.NOON, LocalTime.NOON.plusHours(1), 1L);

        when(eventService.getAllEvents()).thenReturn(List.of(event));
        when(eventMapper.toEventDtoList(List.of(event))).thenReturn(List.of(dto));

        mockMvc.perform(get("/api/v1/event"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1));
    }

    @Test
    void getEventById_success() throws Exception {
        Event event = new Event();
        EventDto dto = new EventDto(1L, "Test", "Desc", LocalDate.now(), LocalTime.NOON, LocalTime.NOON.plusHours(1), 1L);

        when(eventService.getEventById(1L)).thenReturn(event);
        when(eventMapper.toEventDto(event)).thenReturn(dto);

        mockMvc.perform(get("/api/v1/event/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void deleteEventById_success() throws Exception {
        doNothing().when(eventService).deleteEventById(1L);

        mockMvc.perform(delete("/api/v1/event/1"))
                .andExpect(status().isOk());

        verify(eventService, times(1)).deleteEventById(1L);
    }
}
