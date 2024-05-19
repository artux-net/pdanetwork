package net.artux.pdanetwork.controller.rest.statistic;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import net.artux.pdanetwork.models.user.UserStatisticDto;
import net.artux.pdanetwork.service.user.StatisticService;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@Tag(name = "Статистика")
@RequestMapping("/api/v1/statistic")
public class StatisticController {

    private final StatisticService statisticService;

    @GetMapping("/{id}")
    @Operation(summary = "Получить статистику юзера по ID")
    public UserStatisticDto getStatistic(@PathVariable("id") UUID id) {
        return statisticService.getStatistic(id);
    }

    @GetMapping
    @Operation(summary = "Получить статистику")
    public UserStatisticDto getStatistic() {
        return statisticService.getStatistic();
    }

    @PostMapping
    @Operation(summary = "Задать статистику")
    public UserStatisticDto setStatistic(@RequestBody UserStatisticDto dto) {
        return statisticService.setStatistic(dto);
    }

    @PostMapping("/admin/moderate/{id}")
    @Operation(summary = "Задать статистику юзеру")
    public UserStatisticDto getMap(@PathVariable("id") UUID id, @RequestBody UserStatisticDto dto) {
        return statisticService.setStatisticAsModerator(id, dto);
    }


}