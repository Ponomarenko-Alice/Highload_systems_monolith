package com.hs.lab1.mapper;

import com.hs.lab1.dto.EventDto;
import com.hs.lab1.entity.Event;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface EventMapper {
    @Mapping(target = "ownerId", source = "owner.id")
    EventDto toEventDto(Event Event);
    List<EventDto> toEventDtoList(List<Event> events);
}
