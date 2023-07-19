package net.artux.pdanetwork.controller.rest.admin;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import net.artux.pdanetwork.models.page.ResponsePage;
import net.artux.pdanetwork.service.log.LogService;
import net.artux.pdanetwork.utills.security.IsModerator;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@Tag(name = "Логи", description = "Доступен с роли модератора")
@RequestMapping("/api/v1/admin/logs")
@IsModerator
@RequiredArgsConstructor
public class AdminLogController {

    private final LogService logService;

    @GetMapping
    @Operation(summary = "Получение логов, кол-во строк")
    public ResponsePage<Object> listUsers(@RequestParam("page") Optional<Integer> page, @RequestParam("pageSize") Optional<Integer> pageSize) {
        return logService.getLogs(page, pageSize);
    }

}
