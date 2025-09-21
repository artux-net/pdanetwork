package net.artux.pdanetwork.repository.achievement;

import java.util.UUID;

public interface RepositoryAchDto {
    UUID id();

    String title();

    String name();

    String description();

    String image();

    boolean enabled();

}
