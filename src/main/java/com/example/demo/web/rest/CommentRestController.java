package com.example.demo.web.rest;

import com.example.demo.models.dto.CommentCreationtDto;
import com.example.demo.models.dto.CommentMessageDTO;
import com.example.demo.models.enums.ModelEnum;
import com.example.demo.models.view.CommentViewModel;
import com.example.demo.service.CommentService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api")
public class CommentRestController {
    private final CommentService commentService;

    public CommentRestController(CommentService commentService) {
        this.commentService = commentService;
    }

    @GetMapping("/products/Kommerling")
      public ResponseEntity<List<CommentViewModel>> getCommentsKommerling(){
        return ResponseEntity.ok(commentService.getAllComments(ModelEnum.KOMMERLING));
    }

    @GetMapping("products/VIVA")
    public  ResponseEntity<List<CommentViewModel>> getCommentsVIVA(){
        return  ResponseEntity.ok(commentService.getAllComments(ModelEnum.VIVAPLAST));
    }

    @PostMapping(value = "/products/VIVA", consumes = "application/json", produces = "application/json")
    public ResponseEntity<CommentViewModel> createCommentViva(@AuthenticationPrincipal UserDetails userDetails,
                                                              @RequestBody CommentMessageDTO commentDTO){
        CommentCreationtDto commentCreationtDto = new CommentCreationtDto(
                userDetails.getUsername(),
                commentDTO.getMessage(),
                ModelEnum.VIVAPLAST
        );
        CommentViewModel comment= commentService.createComment(commentCreationtDto);
        return ResponseEntity.created(URI.create("/api/products/VIVA"))
                .body(comment);
    }
    @PostMapping(value = "/products/Kommerling", consumes = "application/json", produces = "application/json")
    public ResponseEntity<CommentViewModel> createComment(@AuthenticationPrincipal UserDetails userDetails,
                                                         @RequestBody CommentMessageDTO commentDTO) {
        CommentCreationtDto commentCreationtDto = new CommentCreationtDto(
                userDetails.getUsername(),
                commentDTO.getMessage(),
                ModelEnum.KOMMERLING
        );
        CommentViewModel comment = commentService.createComment(commentCreationtDto);
        return ResponseEntity
                .created(URI.create("/api/products/Kommerling"))
                .body(comment);
    }
}
