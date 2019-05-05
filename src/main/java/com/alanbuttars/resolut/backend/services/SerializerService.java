package com.alanbuttars.resolut.backend.services;

import com.alanbuttars.resolut.backend.models.Account;
import com.alanbuttars.resolut.backend.models.User;
import com.alanbuttars.resolut.backend.serializers.AccountSerializer;
import com.alanbuttars.resolut.backend.serializers.UserSerializer;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * Service for JSON serialization and deserialization of all application models.
 */
public class SerializerService {

    private static final SerializerService INSTANCE = new SerializerService();

    private final Gson gson;

    private SerializerService() {
        this.gson = new GsonBuilder()//
                .registerTypeAdapter(Account.class, new AccountSerializer())//
                .registerTypeAdapter(User.class, new UserSerializer())//
                .create();
    }

    /**
     * Returns the singleton instance of this class.
     */
    public static SerializerService getInstance() {
        return INSTANCE;
    }

    /**
     * Transforms a given object to a JSON string.
     */
    public String toJson(Object object) {
        return this.gson.toJson(object);
    }

    /**
     * Transforms a given JSON string to a given type.
     */
    public <T> T fromJson(String json, Class<T> clazz) {
        return this.gson.fromJson(json, clazz);
    }
}
