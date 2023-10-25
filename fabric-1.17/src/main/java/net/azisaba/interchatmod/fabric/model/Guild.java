package net.azisaba.interchatmod.fabric.model;

import java.util.Objects;

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
