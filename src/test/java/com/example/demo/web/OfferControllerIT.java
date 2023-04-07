package com.example.demo.web;

import com.example.demo.models.entity.*;
import com.example.demo.models.enums.ChamberEnum;
import com.example.demo.models.enums.ColorsEnum;
import com.example.demo.models.enums.ModelEnum;
import com.example.demo.models.enums.UserRoleEnum;
import com.example.demo.util.TestDataUtils;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class OfferControllerIT {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private TestDataUtils testDataUtils;
    private User testUser, testUser2;
    private Window testOffer;

    @BeforeEach
    void setUp() {
        testUser2 = testDataUtils.createTestUser("pesho");
        testOffer = testDataUtils.createTestOffer(testUser2);
    }


    @AfterEach
    void tearDown() {
        testDataUtils.cleanUpDatabase();
    }

    ;

    @Test
    void testUserRegistration() throws Exception {
        mockMvc.perform(post("/users/register")
                        .param("username", "pesho2").
                        param("email", "a@a2").
                        param("fullName", "Pesho Petrov").
                        param("password", "123").
                        param("confirmPassword", "123")
                        .with(csrf())).
                andExpect(status().is3xxRedirection()).
                andExpect(redirectedUrl("/"));

    }


    @Test
    void testDelete() throws Exception {
        mockMvc.perform(delete("/cart/deleteWindow/1", testOffer.getId())
                        .with(csrf())).
                andExpect(status().is3xxRedirection()).
                andExpect(redirectedUrl("http://localhost/users/login"));
    }

    @Test
    @WithMockUser(
            username = "pesho",
            roles = "USER"
    )
    void testLoginDelete() throws Exception {

        mockMvc.perform(delete("/cart/deleteWindow/{id}", testOffer.getId())
                        .with(csrf())).
                andExpect(status().is3xxRedirection()).
                andExpect(redirectedUrl("/cart"));
    }

    @Test
    @WithMockUser(
            username = "pesho",
            roles = "USER"
    )
    void testThrowError403() throws Exception {

        mockMvc.perform(get("/statistics")
                        .with(csrf())).
                andExpect(status().isForbidden());
    }
    @Test
    @WithMockUser(
            username = "pesho",
            roles = "USER"
    )
    void testThrowError404() throws Exception {

        mockMvc.perform(get("/cart/details/100")
                        .with(csrf())).
                andExpect(status().is4xxClientError());
    }

}
