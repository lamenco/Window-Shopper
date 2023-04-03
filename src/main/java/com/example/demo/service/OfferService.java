package com.example.demo.service;

import com.example.demo.models.dto.DoorOfferDto;
import com.example.demo.models.dto.WindowOfferDto;
import com.example.demo.models.entity.Door;
import com.example.demo.models.entity.Window;
import com.example.demo.models.user.WindowShopperUserDetails;

public interface OfferService {
    void addWindow(WindowOfferDto windowOfferDto, WindowShopperUserDetails userDetails);

    void addDoor(DoorOfferDto doorOfferDto, WindowShopperUserDetails userDetails);

    Window findById(Long id);

    boolean isOwner(String username,Long id);

    void deleteWindowById(Long id);

     void calculatePriceWindow(WindowOfferDto windowOfferDto, Window window);
     void calculatePriceDoor(DoorOfferDto doorOfferDto, Door door);

    void deleteDoorById(Long id);

    Door findDoorById(Long id);
}
