package com.example.demo.service.impl;

import com.example.demo.models.entity.Door;
import com.example.demo.models.view.DoorViewModel;
import com.example.demo.models.view.WindowViewModel;
import com.example.demo.repository.DoorOfferRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DoorServiceImpl {
    private final DoorOfferRepository doorOfferRepository;
    private final ModelMapper modelMapper;

    public DoorServiceImpl(DoorOfferRepository doorOfferRepository, ModelMapper modelMapper) {
        this.doorOfferRepository = doorOfferRepository;
        this.modelMapper = modelMapper;
    }


    public List<DoorViewModel> findDoorsByUsername(String username) {
        return doorOfferRepository.findByUserUsername(username).stream()
                .map(door -> {
                    DoorViewModel doorViewModel = modelMapper.map(door, DoorViewModel.class);
                    doorViewModel.setImage(String.format("/images/%s.jpg", doorViewModel.getColor().name().toLowerCase()));
                    return doorViewModel;
                }).collect(Collectors.toList());
    }


    public Door findDoorsById(Long id) {
        Door door = doorOfferRepository.findById(id).orElse(null);
        return door;
    }
}
