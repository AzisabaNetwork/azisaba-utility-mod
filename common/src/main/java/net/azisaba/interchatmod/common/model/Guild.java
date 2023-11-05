package net.azisaba.interchatmod.common.model;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public final class Guild {
    private final long id;
    private final String name;
    private final String format;
    private final int capacity;
    private final boolean open;
    private final boolean deleted;

    public Guild(long id, String name, String format, int capacity, boolean open, boolean deleted) {
        this.id = id;
        this.name = name;
        this.format = format;
        this.capacity = capacity;
        this.open = open;
        this.deleted = deleted;
    }

    @NotNull
    public static Set<Guild> getGuildsFromArray(JsonArray arr) {
        Set<Guild> localGuilds = new HashSet<>();
        for (JsonElement element : arr) {
            JsonObject obj = element.getAsJsonObject();
            localGuilds.add(
                    new Guild(
                            obj.get("id").getAsLong(),
                            obj.get("name").getAsString(),
                            obj.get("format").getAsString(),
                            obj.get("capacity").getAsInt(),
                            obj.get("open").getAsBoolean(),
                            obj.get("deleted").getAsBoolean()
                    )
            );
        }
        return localGuilds;
    }

    public long id() {
        return id;
    }

    public String name() {
        return name;
    }

    public String format() {
        return format;
    }

    public int capacity() {
        return capacity;
    }

    public boolean open() {
        return open;
    }

    public boolean deleted() {
        return deleted;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        Guild that = (Guild) obj;
        return this.id == that.id &&
                Objects.equals(this.name, that.name) &&
                Objects.equals(this.format, that.format) &&
                this.capacity == that.capacity &&
                this.open == that.open &&
                this.deleted == that.deleted;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, format, capacity, open, deleted);
    }

    @Override
    public String toString() {
        return "Guild[" +
                "id=" + id + ", " +
                "name=" + name + ", " +
                "format=" + format + ", " +
                "capacity=" + capacity + ", " +
                "open=" + open + ", " +
                "deleted=" + deleted + ']';
    }

}
