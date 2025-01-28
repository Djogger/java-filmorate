package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/users")
@AllArgsConstructor
public class UserController {
    private final InMemoryUserStorage inMemoryUserStorage;
    private final UserService userService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Collection<User> getAllUsers() {
        return inMemoryUserStorage.getAllUsers();
    }

    @GetMapping("/{userId}/friends")
    @ResponseStatus(HttpStatus.OK)
    public Collection<Long> getFriends(@PathVariable Long userId) {
        return userService.findFriends(userId);
    }

    @GetMapping("/{userId}/friends/common/{friendId}")
    @ResponseStatus(HttpStatus.OK)
    public Collection<Long> getCommonFriends(@PathVariable Long userId, @PathVariable Long friendId) {
        return userService.findCommonFriends(userId, friendId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    public User addUser(@Valid @RequestBody User user) {
        return inMemoryUserStorage.addUser(user);
    }

    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    public User updateUser(@Valid @RequestBody User newUser) {
        return inMemoryUserStorage.updateUser(newUser);
    }

    @PutMapping("/{userId}/friends/{friendId}")
    @ResponseStatus(HttpStatus.OK)
    public Collection<Map<String, Long>> addFriend(@PathVariable Long userId, @PathVariable Long friendId) {
        Collection<Long> response = userService.addFriend(userId, friendId);

        Collection<Map<String, Long>> response2 = new ArrayList<>();
        
        for (Long id : response) {
            Map<String, Long> friendMap = new HashMap<>();
            friendMap.put("id", id);
            response2.add(friendMap);
        }

        return response2;
    }

    @DeleteMapping("/{userId}/friends/{friendId}")
    @ResponseStatus(HttpStatus.OK)
    public User deleteFriend(@PathVariable Long userId, @PathVariable Long friendId) {
        return userService.deleteFriend(userId, friendId);
    }

}