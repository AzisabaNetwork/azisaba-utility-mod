package net.azisaba.interchatmod.fabric.model;

public record Guild(long id, String name, String format, int capacity, boolean open, boolean deleted) {
}
