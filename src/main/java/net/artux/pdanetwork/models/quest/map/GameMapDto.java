package net.artux.pdanetwork.models.quest.map;

public record GameMapDto(int id, String name, String tmx, Position defaultPosition, String title, String background) {
}
