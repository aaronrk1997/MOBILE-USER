package com.tm.user;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tm.user.model.User;
import com.tm.user.repository.UserRepository;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerUnitTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserRepository userRepository;

    private User user1 = new User("User", "One", new Date());
    private User user2 = new User("User", "Two", new Date());

    private List<User> allUsers = Arrays.asList(user1, user2);

    @Test
    public void getAllUsers() throws Exception {
        given(userRepository.findAll()).willReturn(allUsers);

        mockMvc.perform(get("/users")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].name", is(user1.getName())))
                .andExpect(jsonPath("$[0].firstName", is(user1.getFirstName())))
                .andExpect(jsonPath("$[1].name", is(user2.getName())))
                .andExpect(jsonPath("$[1].firstName", is(user2.getFirstName())));
    }

    @Test
    public void getUserByName() throws Exception {
        given(userRepository.findByName(user1.getName())).willReturn(user1);

        mockMvc.perform(get("/users/{name}", user1.getName())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name", is(user1.getName())))
                .andExpect(jsonPath("$.firstName", is(user1.getFirstName())));
    }

    @Test
    public void createUser() throws Exception {
        given(userRepository.save(user1)).willReturn(user1);

        mockMvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(user1)))
                .andExpect(status().isOk());
    }

    @Test
    public void updateUser() throws Exception {
        given(userRepository.save(user1)).willReturn(user1);

        mockMvc.perform(put("/users/{name}", user1.getName())
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(user1)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name", is(user1.getName())))
                .andExpect(jsonPath("$.firstName", is(user1.getFirstName())));
    }

    @Test
    public void deleteUser() throws Exception {
        given(userRepository.findByName(user1.getName())).willReturn(user1);

        mockMvc.perform(delete("/users/{name}", user1.getName())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }


    public static String asJsonString(final Object obj) {
        try {
            final ObjectMapper mapper = new ObjectMapper();
            final String jsonContent = mapper.writeValueAsString(obj);
            return jsonContent;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }  

    
}
