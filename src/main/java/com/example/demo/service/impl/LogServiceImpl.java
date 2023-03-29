package com.example.demo.service.impl;

import com.example.demo.models.entity.LogEntity;
import com.example.demo.models.entity.User;
import com.example.demo.models.service.LogServiceModel;
import com.example.demo.repository.LogRepository;
import com.example.demo.service.LogService;
import com.example.demo.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class LogServiceImpl implements LogService {
    private final LogRepository logRepository;
    private final UserService userService;
    private final ModelMapper modelMapper;

    public LogServiceImpl(LogRepository logRepository, UserService userService, ModelMapper modelMapper) {
        this.logRepository = logRepository;
        this.userService = userService;
        this.modelMapper = modelMapper;
    }

    @Override
    public void createLog(String action) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        String username = authentication.getName();
        User user = userService.findByUsername(username);

        LogEntity logEntity = new LogEntity(user, action, LocalDateTime.now());
        logRepository.save(logEntity);

    }

    @Override
    public List<LogServiceModel> findAllLogs() {
        return logRepository.findAll().stream()
                .map(logEntity -> {
                    LogServiceModel logServiceModel = modelMapper.map(logEntity, LogServiceModel.class);
                    logServiceModel.setUser(logEntity.getUser().getUsername());
                    return logServiceModel;
                }).collect(Collectors.toList());
    }
}
