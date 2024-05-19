package net.artux.pdanetwork.models.enums;

import net.artux.pdanetwork.models.quest.map.Position;

public interface MapEnumGetter {

    int getId();

    String getTmx();

    String getName();

    String getBackground();

    String getTitle();

    Position getDefaultPosition();

}
