package com.alanbuttars.resolut.backend.serializers;

import com.alanbuttars.resolut.backend.models.Account;
import com.google.gson.*;

import java.lang.reflect.Type;
import java.util.Collection;

/**
 * Custom JSON serializer for {@link Account}s.
 */
public class AccountSerializer implements JsonSerializer<Account> {

    @Override
    public JsonElement serialize(Account account, Type type, JsonSerializationContext jsonSerializationContext) {
        JsonObject accountJson = new Gson().toJsonTree(account).getAsJsonObject();
        accountJson.add("links", new LinkSerializer().serialize(account));

        return accountJson;
    }

    public JsonElement serialize(Collection<Account> accounts, JsonSerializationContext jsonSerializationContext) {
        JsonArray accountsJson = new JsonArray();
        accounts.forEach(account -> {
            accountsJson.add(serialize(account, Account.class, jsonSerializationContext));
        });
        return accountsJson;
    }
}
