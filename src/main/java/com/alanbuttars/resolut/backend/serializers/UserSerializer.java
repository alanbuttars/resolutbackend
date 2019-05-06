package com.alanbuttars.resolut.backend.serializers;

import com.alanbuttars.resolut.backend.models.Account;
import com.alanbuttars.resolut.backend.models.User;
import com.google.gson.*;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

/**
 * Custom JSON serializer for {@link User}s.
 */
public class UserSerializer implements JsonSerializer<User>, JsonDeserializer<User> {

    @Override
    public JsonElement serialize(User user, Type type, JsonSerializationContext jsonSerializationContext) {
        JsonObject userJson = new JsonObject();
        userJson.addProperty("id", user.getId());
        userJson.add("accounts", new AccountSerializer().serialize(user.getAccounts().values(), jsonSerializationContext));
        userJson.add("links", new LinkSerializer().serialize(user));

        return userJson;
    }

    @Override
    public User deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        JsonObject userJson = jsonElement.getAsJsonObject();
        String id = userJson.get("id").getAsString();
        List<Account> accounts = new Gson().fromJson(userJson.getAsJsonArray("accounts"), new TypeToken<List<Account>>() {
        }.getType());

        return new User(id, accounts);
    }
}
