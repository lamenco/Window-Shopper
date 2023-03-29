package com.example.demo.web;

import com.example.demo.models.dto.DoorOfferDto;
import com.example.demo.models.dto.UserRegisterDto;
import com.example.demo.models.dto.WindowOfferDto;
import com.example.demo.models.entity.Window;
import com.example.demo.models.user.WindowShopperUserDetails;
import com.example.demo.service.OfferService;
import com.example.demo.service.impl.WindowServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
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

    private final WindowServiceImpl windowService;

    public OfferController(OfferService offerService, WindowServiceImpl windowService) {
        this.offerService = offerService;
        this.windowService = windowService;
    }

    @GetMapping("/calculate")
    public String offer() {
        return "calculator";
    }


    @GetMapping("/door/add")
    public String addDoorOffer(Model model) {
        if(!model.containsAttribute("doorOfferDto")){
            model.addAttribute("doorOfferDto",new DoorOfferDto());
        }
        return "door-add";
    }

    @PostMapping("/door/add")
    public String addOffer(@Valid DoorOfferDto doorOfferDto,
                           BindingResult bindingResult, RedirectAttributes redirectAttributes,
                           @AuthenticationPrincipal WindowShopperUserDetails userDetails){

        if (bindingResult.hasErrors()){
            redirectAttributes.addFlashAttribute("doorOfferDto", doorOfferDto);
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.doorOfferDto",bindingResult);
            return "redirect:/offer/door/add";
        }

            offerService.addDoor(doorOfferDto,userDetails);
        return "redirect:/";

    }

    @GetMapping("/window/add")
    public String addOffer(Model model) {
        if(!model.containsAttribute("windowOfferDto")){
            model.addAttribute("windowOfferDto",new WindowOfferDto());
        }
        return "window-add";
    }

    @PostMapping("/window/add")
    public String addOffer(@Valid WindowOfferDto windowOfferDto,
                           BindingResult bindingResult, RedirectAttributes redirectAttributes,
                           @AuthenticationPrincipal WindowShopperUserDetails userDetails){

        if (bindingResult.hasErrors()){
            redirectAttributes.addFlashAttribute("windowOfferDto", windowOfferDto);
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.windowOfferDto",bindingResult);
            return "redirect:/offer/window/add";
        }

        offerService.addWindow(windowOfferDto,userDetails);
        return "redirect:/";

    }

    @GetMapping("/update/{id}")
    private String update(@PathVariable Long id, Model model){
        Window window =  windowService.findWindowsById(id);
        model.addAttribute("window", window);
        return "edit";
    }
    @PatchMapping("/update/{id}")
    private String updateConfirm(@PathVariable Long id, @Valid WindowOfferDto windowOfferDto,
                                 BindingResult bindingResult,
                                 RedirectAttributes redirectAttributes){
        if (bindingResult.hasErrors()){
            redirectAttributes.addFlashAttribute("windowOfferDto", windowOfferDto);
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.windowOfferDto",bindingResult);
            return "redirect:/offer/update/{id}";
        }

        return "redirect:/";
    }
}
