package net.artux.pdanetwork.service.user;

import net.artux.pdanetwork.models.user.UserStatisticDto;

import java.util.UUID;

public interface StatisticService {

    UserStatisticDto getStatistic();

    UserStatisticDto getStatistic(UUID userId);

    UserStatisticDto setStatistic(UserStatisticDto userStatisticDto);

    UserStatisticDto setStatisticAsModerator(UUID userId, UserStatisticDto userStatisticDto);

}
