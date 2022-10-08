package net.artux.pdanetwork.controller.admin;

import net.artux.pdanetwork.models.user.dto.SimpleUserDto;
import net.artux.pdanetwork.service.profile.ProfileService;
import net.artux.pdanetwork.service.user.UserService;
import net.artux.pdanetwork.service.util.ValuesService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
@RequestMapping("/utility/users")
public class AdminUsersController extends BaseUtilityController {

    private final ProfileService profileService;
    private final UserService userService;
    private final ValuesService valuesService;

    public AdminUsersController(ProfileService profileService, UserService userService, ValuesService valuesService) {
        super("Пользователи");
        this.profileService = profileService;
        this.userService = userService;
        this.valuesService = valuesService;
    }

    @Override
    protected Object getHome(Model model) {
        return redirect(getPageUrl() + "list", model, null);
    }

    @GetMapping("/list")
    public String listUsers(
            Model model, @RequestParam("page") Optional<Integer> page, @RequestParam("size") Optional<Integer> size) {
        int currentPage = page.orElse(1);
        int pageSize = size.orElse(20);

        Page<SimpleUserDto> userPage = profileService.getPage(PageRequest.of(currentPage - 1, pageSize));

        model.addAttribute("userPage", userPage);

        int totalPages = userPage.getTotalPages();
        if (totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
                    .boxed()
                    .collect(Collectors.toList());
            model.addAttribute("pageNumbers", pageNumbers);
        }

        return pageWithContent("users/main", model);
    }

    @GetMapping("/{id}")
    public String getUser(Model model, @PathVariable UUID id){
        model.addAttribute("userEntity", userService.getUserById(id));
        return pageWithContent("users/view", model);
    }

    @DeleteMapping("/{id}")
    public Object deleteUser(Model model, @PathVariable UUID id){
        userService.deleteUserById(id);
        return redirect(getPageUrl(), model, null);
    }

}
