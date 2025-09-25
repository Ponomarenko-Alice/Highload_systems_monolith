package com.hs.lab1.service;

import com.hs.lab1.repository.GroupEventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GroupEventService {
    private final GroupEventRepository groupEventRepository;
}
