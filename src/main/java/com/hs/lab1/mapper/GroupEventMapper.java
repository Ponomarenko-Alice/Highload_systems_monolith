package com.hs.lab1.mapper;

import com.hs.lab1.dto.GroupEventDto;
import com.hs.lab1.entity.GroupEvent;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
        uses = {UserMapper.class})
public interface GroupEventMapper {
    @Mapping(source = "owner.id", target = "ownerId")
    GroupEventDto toGroupEventDto(GroupEvent groupEvent);

    List<GroupEventDto> toGroupEventDtoList(List<GroupEvent> groupEvents);
}
