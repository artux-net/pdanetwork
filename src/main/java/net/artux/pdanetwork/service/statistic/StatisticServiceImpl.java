package net.artux.pdanetwork.service.statistic;

import lombok.RequiredArgsConstructor;
import net.artux.pdanetwork.models.statistic.StatisticDto;
import net.artux.pdanetwork.repository.user.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class StatisticServiceImpl implements StatisticService {

    private final UserRepository repository;

    @Override
    public long countUsers() {
        return repository.count();
    }

    @Override
    public long countRegistrationsToday() {
        return repository.countAllByRegistrationAfter(Instant.now().minus(24, ChronoUnit.HOURS));
    }

    @Override
    public long countOnlineToday() {
        return repository.countAllByLastLoginAtAfter(Instant.now().minus(24, ChronoUnit.HOURS));
    }

    @Override
    public long countOnlineNow() {
        return repository.countAllByLastLoginAtAfter(Instant.now().minus(5, ChronoUnit.MINUTES));
    }

    @Override
    public StatisticDto getStatistic() {
        return new StatisticDto(countUsers(), countRegistrationsToday(), countOnlineToday(), countOnlineNow(), Instant.now());
    }


}
