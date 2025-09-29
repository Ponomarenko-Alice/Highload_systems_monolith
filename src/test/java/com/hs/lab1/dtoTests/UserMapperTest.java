package com.hs.lab1.mapperTests;

import com.hs.lab1.dto.UserDto;
import com.hs.lab1.entity.User;
import com.hs.lab1.mapper.UserMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class UserMapperTest {

    private UserMapper userMapper;

    @BeforeEach
    void setUp() {
        userMapper = Mappers.getMapper(UserMapper.class);
    }

    @Test
    void testToUserDto() {
        User user = new User();
        user.setId(1L);
        user.setUsername("john_doe");
        user.setName("John");
        user.setSurname("Doe");

        UserDto dto = userMapper.toUserDto(user);

        assertNotNull(dto);
        assertEquals(user.getId(), dto.id());
        assertEquals(user.getUsername(), dto.username());
        assertEquals(user.getName(), dto.name());
        assertEquals(user.getSurname(), dto.surname());
        assertNull(dto.eventNames());
    }

    @Test
    void testToUserDtoList() {
        User user1 = new User();
        user1.setId(1L);
        user1.setUsername("john_doe");

        User user2 = new User();
        user2.setId(2L);
        user2.setUsername("jane_doe");

        List<UserDto> dtoList = userMapper.toUserDtoList(List.of(user1, user2));

        assertNotNull(dtoList);
        assertEquals(2, dtoList.size());
        assertEquals("john_doe", dtoList.get(0).username());
        assertEquals("jane_doe", dtoList.get(1).username());
    }
}
