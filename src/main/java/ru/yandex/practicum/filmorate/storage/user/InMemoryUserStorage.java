package ru.yandex.practicum.filmorate.storage.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.*;

@Slf4j
@Component
public class InMemoryUserStorage implements UserStorage {

    private Map<Long, User> users = new HashMap<>();

    private static long id = 1;

    @Override
    public User addUser(User user) {
        if (user.getFriends() == null) {
            user.setFriends(new HashSet<>());
        }
        user.setId(id);
        users.put(id++, user);
        log.info("Пользователь {} успешно добавлен.", user.getLogin());
        return user;
    }

    @Override
    public User updateUser(User user) {
        if (users.containsKey(user.getId())) {
            users.remove(user.getId());
            users.put(user.getId(), user);
            log.info("Информация о пользователе {} успешно обновлена.", user.getLogin());
        } else {
            throw new NotFoundException("пользователь");
        }
        return user;
    }

    @Override
    public User getUserById(Long id) {
        if (users.containsKey(id)) {
            return users.get(id);
        } else {
            throw new NotFoundException("пользователь");
        }
    }

    @Override
    public List<User> getAllUsers() {
        return new ArrayList<>(users.values());
    }

    public User removeUserById(Integer id) {
        if (users.containsKey(id)) {
            return users.remove(id);
        } else {
            throw new NotFoundException("пользователь");
        }
    }
}
