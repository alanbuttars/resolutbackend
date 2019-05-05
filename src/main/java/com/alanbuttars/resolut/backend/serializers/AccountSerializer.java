package com.alanbuttars.resolut.backend.serializers;

import com.alanbuttars.resolut.backend.models.Account;
import com.google.gson.*;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Custom JSON serializer for {@link Account}s.
 */
public class AccountSerializer implements JsonSerializer<Account>, JsonDeserializer<Account> {

    @Override
    public JsonElement serialize(Account account, Type type, JsonSerializationContext jsonSerializationContext) {
        JsonObject accountJson = new JsonObject();
        accountJson.addProperty("id", account.getId());
        accountJson.addProperty("userId", account.getUserId());
        accountJson.addProperty("balance", account.getBalance());
        return accountJson;
    }

    public JsonArray serialize(Collection<Account> accounts, JsonSerializationContext jsonSerializationContext) {
        JsonArray accountsJson = new JsonArray();
        accounts.forEach(account -> {
            accountsJson.add(serialize(account, Account.class, jsonSerializationContext));
        });
        return accountsJson;
    }

    @Override
    public Account deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        JsonObject accountJson = jsonElement.getAsJsonObject();
        String id = accountJson.get("id").getAsString();
        String userId = accountJson.get("userId").getAsString();
        int balance = accountJson.getAsJsonPrimitive("balance").getAsInt();

        return new Account(id, userId, balance);
    }

    public List<Account> deserialize(JsonArray jsonArray, JsonDeserializationContext jsonDeserializationContext) {
        List<Account> accounts = new ArrayList<>();
        jsonArray.forEach(jsonElement -> {
            accounts.add(deserialize(jsonElement, Account.class, jsonDeserializationContext));
        });
        return accounts;
    }
}
