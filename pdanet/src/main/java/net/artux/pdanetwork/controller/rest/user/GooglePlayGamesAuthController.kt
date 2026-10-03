package net.artux.pdanetwork.controller.rest.user

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import net.artux.pdanetwork.models.user.dto.AuthTokenDto
import net.artux.pdanetwork.models.user.dto.GooglePlayGamesAuthDto
import net.artux.pdanetwork.service.user.GooglePlayGamesAuthService
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@Tag(name = "Пользователь")
@RestController
@CrossOrigin
@RequestMapping("/api/v1/user/auth")
class GooglePlayGamesAuthController(
    private val googlePlayGamesAuthService: GooglePlayGamesAuthService,
) {

    @Operation(summary = "Вход/регистрация через Google Play Games")
    @PostMapping("/google-play-games")
    fun authenticateWithGooglePlayGames(@RequestBody dto: GooglePlayGamesAuthDto): AuthTokenDto {
        return googlePlayGamesAuthService.authenticate(dto.serverAuthCode)
    }
}
