package net.artux.pdanetwork.service.user;

import lombok.RequiredArgsConstructor;
import net.artux.pdanetwork.entity.user.StatisticEntity;
import net.artux.pdanetwork.entity.mappers.UserMapper;
import net.artux.pdanetwork.models.user.UserStatisticDto;
import net.artux.pdanetwork.repository.user.StatisticRepository;
import net.artux.pdanetwork.utills.security.ModeratorAccess;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class StatisticServiceImpl implements StatisticService {

    private final UserService userService;
    private final StatisticRepository repository;
    private final UserMapper userMapper;
    private final Logger logger = LoggerFactory.getLogger(StatisticService.class);

    @Override
    public UserStatisticDto getStatistic() {
        return userMapper.dto(repository.findByUser_Id(userService.getCurrentId()));
    }

    @Override
    public UserStatisticDto getStatistic(UUID userId) {
        return userMapper.dto(repository.findByUser_Id(userId));
    }

    @Override
    public UserStatisticDto setStatistic(UserStatisticDto userStatisticDto) {
        StatisticEntity entity = repository.findByUser_Id(userService.getCurrentId());

        if (entity.collectedArtifacts < userStatisticDto.collectedArtifacts)
            entity.collectedArtifacts = userStatisticDto.collectedArtifacts;
        if (entity.distance < userStatisticDto.distance)
            entity.distance = userStatisticDto.distance;
        if (entity.killedEnemies < userStatisticDto.killedEnemies)
            entity.killedEnemies = userStatisticDto.killedEnemies;
        if (entity.killedMutants < userStatisticDto.killedMutants)
            entity.killedMutants = userStatisticDto.killedMutants;
        if (entity.secretFound < userStatisticDto.secretFound)
            entity.secretFound = userStatisticDto.secretFound;

        return userMapper.dto(repository.save(entity));
    }

    @Override
    @ModeratorAccess
    public UserStatisticDto setStatisticAsModerator(UUID userId, UserStatisticDto userStatisticDto) {
        StatisticEntity entity = repository.findByUser_Id(userId);
        userMapper.entity(userStatisticDto);
        return userMapper.dto(repository.save(entity));
    }
}
