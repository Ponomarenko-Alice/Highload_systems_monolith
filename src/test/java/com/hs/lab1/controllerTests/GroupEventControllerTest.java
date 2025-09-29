package com.hs.lab1.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.hs.lab1.dto.GroupEventDto;
import com.hs.lab1.entity.GroupEvent;
import com.hs.lab1.enums.GroupEventStatus;
import com.hs.lab1.mapper.GroupEventMapper;
import com.hs.lab1.mapper.UserMapper;
import com.hs.lab1.requests.CreateGroupEventRequest;
import com.hs.lab1.service.GroupEventService;
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

class GroupEventControllerTest {

    private MockMvc mockMvc;

    @Mock
    private GroupEventService groupEventService;

    @Mock
    private GroupEventMapper groupEventMapper;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private GroupEventController groupEventController;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(groupEventController).build();
        objectMapper.registerModule(new JavaTimeModule());
    }

    @Test
    void addGroupEvent_success() throws Exception {
        CreateGroupEventRequest request = new CreateGroupEventRequest(
                "GroupTest", "Desc",
                LocalDate.now(), LocalTime.NOON, LocalTime.NOON.plusHours(1),
                List.of(1L, 2L), 10L, GroupEventStatus.PENDING
        );

        GroupEvent groupEvent = new GroupEvent();
        GroupEventDto dto = new GroupEventDto(1L, "GroupTest", "Desc",
                LocalDate.now(), LocalTime.NOON, LocalTime.NOON.plusHours(1),
                List.of(), 10L, GroupEventStatus.PENDING);

        when(groupEventService.addGroupEvent(anyString(), anyString(), any(), any(), any(),
                anyList(), anyLong(), any())).thenReturn(groupEvent);
        when(groupEventMapper.toGroupEventDto(groupEvent)).thenReturn(dto);

        mockMvc.perform(post("/api/v1/groupEvent")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1));

        verify(groupEventService, times(1)).addGroupEvent(anyString(), anyString(), any(), any(), any(),
                anyList(), anyLong(), any());
    }

    @Test
    void getAllGroupEvents_success() throws Exception {
        GroupEvent groupEvent = new GroupEvent();
        GroupEventDto dto = new GroupEventDto(1L, "GroupTest", "Desc",
                LocalDate.now(), LocalTime.NOON, LocalTime.NOON.plusHours(1),
                List.of(), 10L, GroupEventStatus.PENDING);

        when(groupEventService.getAllGroupEvents()).thenReturn(List.of(groupEvent));
        when(groupEventMapper.toGroupEventDtoList(List.of(groupEvent))).thenReturn(List.of(dto));

        mockMvc.perform(get("/api/v1/groupEvent"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1));
    }

    @Test
    void getGroupEventById_success() throws Exception {
        GroupEvent groupEvent = new GroupEvent();
        GroupEventDto dto = new GroupEventDto(1L, "GroupTest", "Desc",
                LocalDate.now(), LocalTime.NOON, LocalTime.NOON.plusHours(1),
                List.of(), 10L, GroupEventStatus.PENDING);

        when(groupEventService.getGroupEventById(1L)).thenReturn(groupEvent);
        when(groupEventMapper.toGroupEventDto(groupEvent)).thenReturn(dto);

        mockMvc.perform(get("/api/v1/groupEvent/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void deleteGroupEventById_success() throws Exception {
        doNothing().when(groupEventService).deleteGroupEventById(1L);

        mockMvc.perform(delete("/api/v1/groupEvent/1"))
                .andExpect(status().isOk());

        verify(groupEventService, times(1)).deleteGroupEventById(1L);
    }
}
