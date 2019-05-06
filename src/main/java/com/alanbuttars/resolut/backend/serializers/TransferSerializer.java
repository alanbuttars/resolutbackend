package com.alanbuttars.resolut.backend.serializers;

import com.alanbuttars.resolut.backend.models.Transfer;
import com.google.gson.*;

import java.lang.reflect.Type;

/**
 * Custom JSON serializer for {@link Transfer}s.
 */
public class TransferSerializer implements JsonSerializer<Transfer> {

    @Override
    public JsonElement serialize(Transfer transfer, Type type, JsonSerializationContext jsonSerializationContext) {
        JsonObject transferJson = new Gson().toJsonTree(transfer).getAsJsonObject();
        transferJson.add("links", new LinkSerializer().serialize(transfer));

        return transferJson;
    }
}
