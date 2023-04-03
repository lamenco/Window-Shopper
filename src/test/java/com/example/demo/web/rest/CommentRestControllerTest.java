package com.example.demo.web.rest;

import com.example.demo.models.dto.CommentCreationtDto;
import com.example.demo.models.dto.CommentMessageDTO;
import com.example.demo.models.entity.Comment;
import com.example.demo.models.entity.Model;
import com.example.demo.models.entity.User;
import com.example.demo.models.enums.ModelEnum;
import com.example.demo.models.view.CommentViewModel;
import com.example.demo.service.CommentService;
import com.example.demo.service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
@SpringBootTest
@AutoConfigureMockMvc
class CommentRestControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CommentService commentService;
    private ObjectMapper objectMapper;

    @MockBean
    private UserService userService;

//    @BeforeEach
//    void setUp() {
//        objectMapper = new ObjectMapper();
//    }

    @Test
    @WithMockUser(username = "TestUser1")
    public void getAllComments_request_allCommentsReturned() throws Exception {
        when(commentService.getAllComments(ModelEnum.KOMMERLING)).thenReturn(List.of(
                new CommentViewModel("TestUser1", "test1"),
                new CommentViewModel("TestUser2", "test2")
        ));
        mockMvc.perform(MockMvcRequestBuilders.get("/api/products/Kommerling"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].authorName", is("TestUser1")))
                .andExpect(jsonPath("$.[1].message", is("test1")))
                .andExpect(jsonPath("$.[0].authorName", is("TestUser2")))
                .andExpect(jsonPath("$.[1].message", is("test2")));

    }

    @Test
    public void createComment_anonymousUser_forbidden() throws Exception {
        CommentMessageDTO commentMessageDTO = new CommentMessageDTO();
        commentMessageDTO.setMessage("Text comment");
        mockMvc.perform(MockMvcRequestBuilders.post("/api/products/Kommerling")
                        .content(objectMapper.writeValueAsString(commentMessageDTO)))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "testUsername")
    public void createComment_withTestUser() throws Exception {
        when(commentService.createComment(any())).thenAnswer(interaction -> {
            CommentCreationtDto commentCreationDto = interaction.getArgument(0);
            return new CommentViewModel(commentCreationDto.getUsername(),commentCreationDto.getMessage());
        });
        CommentMessageDTO commentMessageDto = new CommentMessageDTO("This is comment #1");
        ObjectMapper objectMapper = new ObjectMapper();
        mockMvc.perform(post("/api/products/Kommerling")
                        .content(objectMapper.writeValueAsString(commentMessageDto))
                        .with(csrf())
                        .contentType("application/json")
                        .accept("application/json"))
                .andExpect(status().is(201))
                .andExpect(jsonPath("$.authorName", is("testUsername")))
                .andExpect(jsonPath("$.message", is("This is comment #1")));


    }


    private Comment createComment(String text) {
        User author = new User();
        author.setUsername("testUser");
        author.setFullName("Test User");

        Comment comment = new Comment();
        comment.setCreated(LocalDateTime.now());
        comment.setText(text);
        comment.setAuthor(author);
        comment.setModel(new Model(ModelEnum.KOMMERLING));
        return comment;

    }
}