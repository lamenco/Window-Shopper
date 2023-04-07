package com.example.demo.web;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;



import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest
@AutoConfigureMockMvc
public class UserRegistrationControllerIT {

    @Autowired
    private MockMvc mockMvc;


    @Test
    void testRegistrationPageShown() throws Exception {
        mockMvc.perform(get("/users/register")).
                andExpect(status().isOk()).
                andExpect(view().name("register"));
    }


    @Test
    void testLoginPageShown() throws Exception {
        mockMvc.perform(get("/users/login")).
                andExpect(status().isOk()).
                andExpect(view().name("login"));
    }
    @Test
    void testCartPageShown() throws Exception {
        mockMvc.perform(get("/cart")).
                andExpect(status().is3xxRedirection()).
                andExpect(redirectedUrl("http://localhost/users/login"));
    }
    @Test
    void testDoorAddPageShown() throws Exception {
        mockMvc.perform(get("/offer/door/add")).
                andExpect(status().is3xxRedirection()).
                andExpect(redirectedUrl("http://localhost/users/login"));
    }
    @Test
    void testWindowAddPageShown() throws Exception {
        mockMvc.perform(get("/offer/window/add")).
                andExpect(status().is3xxRedirection()).
                andExpect(redirectedUrl("http://localhost/users/login"));
    }
    ;



    ;

}
