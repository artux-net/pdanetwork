package net.artux.pdanetwork.models.statistic;

import java.time.Instant;

public record StatisticDto(
    long users,
    long registrationsToday,
    long onlineToday,
    long onlineNow,
    Instant serverTime) {}
