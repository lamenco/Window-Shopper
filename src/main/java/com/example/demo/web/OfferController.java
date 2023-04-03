package com.example.demo.web;

import com.example.demo.models.dto.DoorOfferDto;
import com.example.demo.models.dto.WindowOfferDto;
import com.example.demo.models.entity.Door;
import com.example.demo.models.entity.Window;
import com.example.demo.models.user.WindowShopperUserDetails;
import com.example.demo.repository.DoorOfferRepository;
import com.example.demo.repository.WindowOfferRepository;
import com.example.demo.service.*;
import com.example.demo.service.impl.DoorServiceImpl;
import com.example.demo.service.impl.WindowServiceImpl;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

@Controller
@RequestMapping("offer")
public class OfferController {
    private final OfferService offerService;

    public final WindowServiceImpl windowService;
    public final DoorServiceImpl doorService;
    private final ModelService modelService;
    private final ChamberService chamberService;
    private final ColorService colorService;
    private final WindowOfferRepository windowOfferRepository;
    private final DoorOfferRepository doorOfferRepository;
    private final MaterialService materialService;


    public OfferController(OfferService offerService, WindowServiceImpl windowService, DoorServiceImpl doorService, ModelService modelService, ChamberService chamberService, ColorService colorService, WindowOfferRepository windowOfferRepository, DoorOfferRepository doorOfferRepository, MaterialService materialService) {
        this.offerService = offerService;
        this.windowService = windowService;
        this.doorService = doorService;
        this.modelService = modelService;
        this.chamberService = chamberService;
        this.colorService = colorService;
        this.windowOfferRepository = windowOfferRepository;
        this.doorOfferRepository = doorOfferRepository;
        this.materialService = materialService;
    }

    @GetMapping("/calculate")
    public String offer() {
        return "calculator";
    }


    @GetMapping("/door/add")
    public String addDoorOffer(Model model) {
        if (!model.containsAttribute("doorOfferDto")) {
            model.addAttribute("doorOfferDto", new DoorOfferDto());
        }
        return "door-add";
    }

    @PostMapping("/door/add")
    public String addOffer(@Valid DoorOfferDto doorOfferDto,
                           BindingResult bindingResult, RedirectAttributes redirectAttributes,
                           @AuthenticationPrincipal WindowShopperUserDetails userDetails) {

        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("doorOfferDto", doorOfferDto);
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.doorOfferDto", bindingResult);
            return "redirect:/offer/door/add";
        }

        offerService.addDoor(doorOfferDto, userDetails);
        return "redirect:/";

    }

    @GetMapping("/window/add")
    public String addOffer(Model model) {
        if (!model.containsAttribute("windowOfferDto")) {
            model.addAttribute("windowOfferDto", new WindowOfferDto());
        }
        return "window-add";
    }

    @PostMapping("/window/add")
    public String addOffer(@Valid WindowOfferDto windowOfferDto,
                           BindingResult bindingResult, RedirectAttributes redirectAttributes,
                           @AuthenticationPrincipal WindowShopperUserDetails userDetails) {

        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("windowOfferDto", windowOfferDto);
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.windowOfferDto", bindingResult);
            return "redirect:/offer/window/add";
        }

        offerService.addWindow(windowOfferDto, userDetails);
        return "redirect:/";

    }

    @GetMapping("/update/{id}")
    public String update(@PathVariable Long id, Model model) {
        Window window = windowService.findWindowsById(id);
        model.addAttribute("window", window);
        return "edit-window";
    }

    @GetMapping("/updateDoor/{id}")
    public String updateDoor(@PathVariable Long id, Model model) {
        Door door = doorService.findDoorsById(id);
        model.addAttribute("door", door);
        return "edit-door";
    }

    @PatchMapping("/update/{id}")
    public String updateConfirm(@PathVariable Long id, @Valid WindowOfferDto windowOfferDto,
                                BindingResult bindingResult,
                                RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("windowOfferDto", windowOfferDto);
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.windowOfferDto", bindingResult);
            return "redirect:/offer/update/{id}";
        }
        Window window = windowService.findWindowsById(id);

        window.setModel(modelService.findByModel(windowOfferDto.getModel()));
        window.setChamber(chamberService.findByChamber(windowOfferDto.getChamber()));
        window.setColor(colorService.findByColor(windowOfferDto.getColor()));
        window.setHeight(windowOfferDto.getHeight());
        window.setWidth(windowOfferDto.getWidth());
        offerService.calculatePriceWindow(windowOfferDto, window);
        windowOfferRepository.save(window);
        return "redirect:/";
    }

    @PatchMapping("/updateDoor/{id}")
    public String updateConfirmDoor(@PathVariable Long id, @Valid DoorOfferDto doorOfferDto,
                                    BindingResult bindingResult,
                                    RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("doorOfferDto", doorOfferDto);
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.doorOfferDto", bindingResult);
            return "redirect:/offer/updateDoor/{id}";
        }
        Door door = doorService.findDoorsById(id);

        door.setModel(modelService.findByModel(doorOfferDto.getModel()));
        door.setChamber(chamberService.findByChamber(doorOfferDto.getChamber()));
        door.setColor(colorService.findByColor(doorOfferDto.getColor()));
        door.setMaterial(materialService.findByMaterial(doorOfferDto.getMaterial()));
        door.setHeight(doorOfferDto.getHeight());
        door.setWidth(doorOfferDto.getWidth());
        offerService.calculatePriceDoor(doorOfferDto, door);
        doorOfferRepository.save(door);
        return "redirect:/";
    }
}
