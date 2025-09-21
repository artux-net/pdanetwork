package net.artux.pdanetwork.repository.achievement;

import java.util.List;
import java.util.UUID;

public interface RepositoryAchCategoryDto {
    UUID id();

    String title();

    String name();

    String description();

    String image();

    List<RepositoryAchDto> achievements();

}
