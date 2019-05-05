package com.alanbuttars.resolut.backend.services;

import com.alanbuttars.resolut.backend.models.Account;
import com.alanbuttars.resolut.backend.models.Result;
import com.alanbuttars.resolut.backend.models.User;

import java.util.*;

/**
 * Service for {@link User} actions.
 */
public class UserService {

    private static final UserService INSTANCE = new UserService();
    private final Map<String, User> usersById;

    /**
     * Initializes the data storage of {@link User}s.
     */
    private UserService() {
        /**
         * Using a TreeMap here for the following reasons:
         *
         * 1. API consistency
         *      - When a user calls GET /users, we return all users
         *      - For API consistency, we should always return users in the same order
         * 2. Performance
         *      - We could sort the keys of a HashMap manually, but that's O(N log N) every time we do so
         *      - It is unlikely that we will insert users at such a pace that the O(log N) insertion time will be a problem
         */
        this.usersById = new TreeMap<>();
        this.usersById.put("alice", new User("alice", Arrays.asList(
                new Account("home", "alice", 1000),
                new Account("work", "alice", 500)
        )));

        this.usersById.put("bob", new User("bob", Arrays.asList(
                new Account("home", "bob", 500)
        )));

        this.usersById.put("eve", new User("eve", Arrays.asList(
                new Account("work", "eve", 2000)
        )));
    }

    /**
     * Returns the singleton instance of this class.
     */
    public static UserService getInstance() {
        return INSTANCE;
    }

    /**
     * Returns the <code>List</code> of {@link User}s.
     */
    public Result<List<User>> listUsers() {
        return Result.success(new ArrayList<>(usersById.values()));
    }

    /**
     * Returns the {@link User} associated with a given <code>userId</code>.
     */
    public Result<User> getUser(String userId) {
        User user = usersById.get(userId);
        if (user == null) {
            return Result.failure("User " + userId + " could not be found");
        }
        return Result.success(user);
    }
}
