package ru.kovalenkojuls.usersorders.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.kovalenkojuls.usersorders.config.TestConfig;
import ru.kovalenkojuls.usersorders.domain.User;
import ru.kovalenkojuls.usersorders.service.UserService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(UserController.class)
@Import(TestConfig.class)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserService userService;

    @Test
    public void getAllUsers() throws Exception {
        User user1 = new User();
        user1.setId(1L);
        user1.setName("User 1");

        User user2 = new User();
        user2.setId(2L);
        user2.setName("User 2");

        when(userService.getAllUsers()).thenReturn(Arrays.asList(user1, user2));

        mockMvc.perform(get("/api/users"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].orders").doesNotExist())
                .andExpect(jsonPath("$[1].orders").doesNotExist());
    }

    @Test
    public void createUser() throws Exception {
        User user = new User(null, "New User", "new.user@example.com", new ArrayList<>());
        User savedUser = new User(1L, "New User", "new.user@example.com", new ArrayList<>());

        when(userService.createUser(any(User.class))).thenReturn(savedUser);

        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                            {"name": "New User", "email": "new.user@example.com"}
                            """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.message").value("User created successfully"));
    }

    @Test
    public void getUser_found() throws Exception {
        User user = new User(1L, "User 1", "user1@example.com", new ArrayList<>());
        when(userService.getUserById(1L)).thenReturn(Optional.of(user));

        mockMvc.perform(get("/api/users/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name").value("User 1"))
                .andExpect(jsonPath("$.orders").isEmpty());
    }

    @Test
    void updateUser_success() throws Exception {
        User updatedUser = new User(1L, "Updated User", "updated.user@example.com", new ArrayList<>());
        when(userService.updateUser(eq(1L), any(User.class))).thenReturn(Optional.of(updatedUser));

        mockMvc.perform(put("/api/users/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                            {"id": 1, "name": "Updated User", "email": "updated.user@example.com"}
                            """))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").value("User updated successfully"))
                .andExpect(jsonPath("$.user.id").value(1))
                .andExpect(jsonPath("$.user.name").value("Updated User"));
    }

    @Test
    void updateUser_notFound() throws Exception {
        User updatedUser = new User(1L, "Updated User", "updated.user@example.com", new ArrayList<>());
        when(userService.updateUser(eq(1L), any(User.class))).thenReturn(Optional.empty());

        mockMvc.perform(put("/api/users/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                            {"id": 1, "name": "Updated User", "email": "updated.user@example.com"}
                            """))
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteUser_success() throws Exception {
        when(userService.deleteUser(1L)).thenReturn(true);

        mockMvc.perform(delete("/api/users/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").value("User deleted successfully"))
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void deleteUser_notFound() throws Exception {
        when(userService.deleteUser(1L)).thenReturn(false);

        mockMvc.perform(delete("/api/users/{id}", 1L))
                .andExpect(status().isNotFound());
    }
}