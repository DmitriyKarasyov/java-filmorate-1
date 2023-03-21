package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NoFriendsException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class UserService {
    @Autowired
    @Qualifier("userDbStorage")
    private UserStorage storage;

    public User addUser(User user) {
        return storage.addUser(user);
    }

    public User updateUser(User user) {
        return storage.updateUser(user);
    }

    public List<User> getAllUsers() {
        return storage.getAllUsers();
    }

    public User getUserById(Long id) {
        return storage.getUserById(id);
    }

    public User addFriend(Long id, Long friendId) {
        User user = storage.getUserById(id);
        storage.getUserById(friendId);
        return storage.updateUser(addFriendIdToUser(user, friendId));
    }

    public User addFriendIdToUser(User user, Long id) {
        Set<Long> friends = user.getFriends();
        if (friends == null) {
            friends = new HashSet<Long>();
        }
        friends.add(id);
        user.setFriends(friends);
        return user;
    }

    public User deleteFriend(Long id, Long friendId) {
        User user = storage.getUserById(id);
        return storage.updateUser(deleteFriendIdFromUser(user, friendId));
    }

    public User deleteFriendIdFromUser(User user, Long id) {
        Set<Long> friends = user.getFriends();
        if (friends == null) {
            throw new NoFriendsException(user.getId());
        }
        if (friends.contains(id)) {
            friends.remove(id);
            user.setFriends(friends);
            return user;
        } else {
            throw new NotFoundException("друг");
        }
    }

    public List<User> getUserFriends(Long id) {
        User user = storage.getUserById(id);
        Set<Long> friendsId = user.getFriends();
        List<User> friends = new ArrayList<>();
        for (Long friendId : friendsId) {
            friends.add(storage.getUserById(friendId));
        }
        return friends;
    }

    public List<User> getCommonFriends(Long id, Long otherId) {
        List<User> commonFriends = new ArrayList<>();
        Set<Long> friendsId = storage.getUserById(id).getFriends();
        Set<Long> otherFriendsId = storage.getUserById(otherId).getFriends();
        if (friendsId == null || otherFriendsId == null) {
            return commonFriends;
        }
        for (Long commonFriendId : findCommon(friendsId, otherFriendsId)) {
            commonFriends.add(storage.getUserById(commonFriendId));
        }
        return commonFriends;
    }

    public Set<Long> findCommon(Set<Long> setA, Set<Long> setB) {
        Set<Long> smallSet;
        Set<Long> bigSet;
        if (setA.size() <= setB.size()) {
            smallSet = new HashSet<>(setA);
            bigSet = new HashSet<>(setB);
        } else {
            smallSet = new HashSet<>(setB);
            bigSet = new HashSet<>(setA);
        }
        smallSet.removeIf(id -> !bigSet.contains(id));
        return smallSet;
    }
}
