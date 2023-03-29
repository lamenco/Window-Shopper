package com.example.demo.service;

import com.example.demo.models.service.LogServiceModel;

import java.util.List;

public interface LogService  {
    void createLog(String action);

    List<LogServiceModel> findAllLogs();
}
