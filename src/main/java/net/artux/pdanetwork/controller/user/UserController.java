package net.artux.pdanetwork.controller.user;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import net.artux.pdanetwork.models.user.dto.RegisterUserDto;
import net.artux.pdanetwork.models.user.dto.UserDto;
import net.artux.pdanetwork.models.Status;
import net.artux.pdanetwork.service.user.UserService;
import org.springframework.web.bind.annotation.*;

@RestController
@Api(tags = "Пользователи")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @ApiOperation(value = "Пользователь")
    @GetMapping("/info")
    public UserDto loginUser() {
        return userService.getMemberDto();
    }

    @ApiOperation(value = "Редактирование информации")
    @PutMapping("/edit")
    public Status editUser(@RequestBody RegisterUserDto user) {
        return userService.editMember(user);
    }


}