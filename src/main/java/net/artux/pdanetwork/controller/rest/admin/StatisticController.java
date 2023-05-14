package net.artux.pdanetwork.controller.rest.admin;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import net.artux.pdanetwork.models.statistic.StatisticDto;
import net.artux.pdanetwork.service.statistic.StatisticService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "Статистика")
@RequestMapping("/api/v1/admin/statistic")
@PreAuthorize("hasAuthority('MODERATOR')")
@RequiredArgsConstructor
public class StatisticController {

    private final StatisticService statisticService;

    @GetMapping
    public StatisticDto getStatistic() {
        return statisticService.getStatistic();
    }
}
