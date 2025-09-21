package net.artux.pdanetwork.service.statistic;

import net.artux.pdanetwork.models.statistic.StatisticDto;

public interface ServerStatisticService {

    long countUsers();

    long countRegistrationsToday();

    long countOnlineToday();

    long countOnlineNow();

    StatisticDto getStatistic();

}
