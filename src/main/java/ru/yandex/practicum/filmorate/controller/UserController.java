package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import javax.validation.Valid;
import java.util.*;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {
    private UserService service;

    @Autowired
    public UserController(UserService service) {
        this.service = service;
    }

    @PostMapping
    public User addUser(@Valid @RequestBody User user) {
        return service.addUser(user);
    }

    @PutMapping
    public User updateUser(@Valid @RequestBody User user) {
        return service.updateUser(user);
    }

    @GetMapping
    public List<User> getAllUsers() {
        return service.getAllUsers();
    }

    @GetMapping("/{id}")
    public User getUserById(@PathVariable Long id) {
        return service.getUserById(id);
    }

    @PutMapping("/{id}/friends/{friendId}")
    public User addFriend(@PathVariable Long id, @PathVariable Long friendId) {
        return service.addFriend(id, friendId);
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public User deleteFriend(@PathVariable Long id, @PathVariable Long friendId) {
        return service.deleteFriend(id, friendId);
    }

    @GetMapping("/{id}/friends")
    public List<User> getUserFriends(@PathVariable Long id) {
        return service.getUserFriends(id);
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public List<User> getCommonFriends(@PathVariable Long id, @PathVariable Long otherId) {
        return service.getCommonFriends(id, otherId);
    }
}
