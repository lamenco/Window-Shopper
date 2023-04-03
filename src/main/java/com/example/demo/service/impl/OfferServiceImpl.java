package com.example.demo.service.impl;

import com.example.demo.exception.ObjectNotFoundException;
import com.example.demo.models.dto.DoorOfferDto;
import com.example.demo.models.dto.WindowOfferDto;
import com.example.demo.models.entity.Door;
import com.example.demo.models.entity.User;
import com.example.demo.models.entity.Window;
import com.example.demo.models.enums.UserRoleEnum;
import com.example.demo.models.user.WindowShopperUserDetails;
import com.example.demo.repository.DoorOfferRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.repository.WindowOfferRepository;
import com.example.demo.service.*;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
public class OfferServiceImpl implements OfferService {
    private final ModelMapper modelMapper;
    private final WindowOfferRepository windowOfferRepository;
    private final DoorOfferRepository doorOfferRepository;
    private final ChamberService chamberService;
    private final ColorService colorService;
    private final MaterialService materialService;
    private final UserService userService;
    private final UserRepository userRepository;
    private final ModelService modelService;

    public OfferServiceImpl(ModelMapper modelMapper, WindowOfferRepository windowOfferRepository, DoorOfferRepository doorOfferRepository, ChamberService chamberService, ColorService colorService, MaterialService materialService, UserService userService, UserRepository userRepository, ModelService modelService) {
        this.modelMapper = modelMapper;
        this.windowOfferRepository = windowOfferRepository;
        this.doorOfferRepository = doorOfferRepository;
        this.chamberService = chamberService;
        this.colorService = colorService;
        this.materialService = materialService;
        this.userService = userService;
        this.userRepository = userRepository;
        this.modelService = modelService;
    }

    @Override
    public void addWindow(WindowOfferDto windowOfferDto, WindowShopperUserDetails userDetails) {
        Window window = modelMapper.map(windowOfferDto, Window.class);
        window.setColor(colorService.findByColor(windowOfferDto.getColor()));
        window.setChamber(chamberService.findByChamber(windowOfferDto.getChamber()));
        window.setUser(userService.findByUsername(userDetails.getUsername()));
        window.setModel(modelService.findByModel(windowOfferDto.getModel()));
        calculatePriceWindow(windowOfferDto, window);
        windowOfferRepository.save(window);
    }


    public void calculatePriceWindow(WindowOfferDto windowOfferDto, Window window) {
        double calculatePrice = windowOfferDto.getHeight() * windowOfferDto.getWidth();
        if (windowOfferDto.getChamber().name().equals("FIVE_CHAMBER")) {
            calculatePrice *= 1.1;
        } else {
            calculatePrice *= 1.3;
        }
        if (windowOfferDto.getColor().name().equals("White")) {
            calculatePrice /= 60;
        } else {
            calculatePrice /= 50;
        }
        BigDecimal price = new BigDecimal(calculatePrice).setScale(2, RoundingMode.DOWN);
        window.setPrice(price);
    }

    @Override
    public void addDoor(DoorOfferDto doorOfferDto, WindowShopperUserDetails userDetails) {
        Door door = modelMapper.map(doorOfferDto, Door.class);
        door.setColor(colorService.findByColor(doorOfferDto.getColor()));
        door.setMaterial(materialService.findByMaterial(doorOfferDto.getMaterial()));
        door.setChamber(chamberService.findByChamber(doorOfferDto.getChamber()));
        door.setUser(userService.findByUsername(userDetails.getUsername()));
        door.setModel(modelService.findByModel(doorOfferDto.getModel()));
        calculatePriceDoor(doorOfferDto, door);
        doorOfferRepository.save(door);
    }

    public  void calculatePriceDoor(DoorOfferDto doorOfferDto, Door door) {
        double calculatePrice = doorOfferDto.getHeight() * doorOfferDto.getWidth();
        if (doorOfferDto.getChamber().name().equals("FIVE_CHAMBER")) {
            calculatePrice *= 1.1;
        } else {
            calculatePrice *= 1.4;
        }
        if (doorOfferDto.getColor().name().equals("White")) {
            calculatePrice /= 60;
        } else {
            calculatePrice /= 50;
        }
        if (doorOfferDto.getMaterial().name().equals("PVC")) {
            calculatePrice *= 1.2;
        } else {
            calculatePrice *= 1.1;
        }
        BigDecimal price = new BigDecimal(calculatePrice).setScale(2, RoundingMode.HALF_UP);
        door.setPrice(price);
    }

    @Override
    public Window findById(Long id) {
        return windowOfferRepository.findById(id).orElseThrow(() -> new ObjectNotFoundException("Offer with ID " + id + " not found"));
    }

    @Override
    public void deleteWindowById(Long id) {
        windowOfferRepository.deleteById(id);
    }

    @Override
    public void deleteDoorById(Long id) {
        doorOfferRepository.deleteById(id);
    }

    @Override
    public Door findDoorById(Long id) {
        return doorOfferRepository.findById(id).orElseThrow(() -> new ObjectNotFoundException("Offer with ID " + id + " not found"));
    }

    public boolean isOwner(String username, Long id) {
        boolean isOwner = windowOfferRepository.findById(id)
                .filter(w -> w.getUser().getUsername().equals(username)).isPresent();

        if(isOwner){
            return true;
        }
        return userRepository.findByUsername(username).filter(this::isAdmin).isPresent();
    }

    private boolean isAdmin(User user){
       return user.getUserRole().stream().anyMatch(r->r.getUserRole()== UserRoleEnum.ADMIN);
    }

}
