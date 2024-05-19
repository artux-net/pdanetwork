package net.artux.pdanetwork.controller.rest.admin;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import net.artux.pdanetwork.models.statistic.StatisticDto;
import net.artux.pdanetwork.service.statistic.ServerStatisticService;
import net.artux.pdanetwork.utills.security.ModeratorAccess;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "Статистика", description = "Доступен с роли модератора")
@RequestMapping("/api/v1/admin/statistic")
@ModeratorAccess
@RequiredArgsConstructor
public class ServerStatisticController {

    private final ServerStatisticService serverStatisticService;

    @GetMapping
    @Operation(description = "Получение текущей статистики")
    public StatisticDto getStatistic() {
        return serverStatisticService.getStatistic();
    }
}
