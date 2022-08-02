package net.artux.pdanetwork.controller.admin;

import lombok.RequiredArgsConstructor;
import net.artux.pdanetwork.entity.user.UserEntity;
import net.artux.pdanetwork.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;
import springfox.documentation.annotations.ApiIgnore;

import java.text.SimpleDateFormat;
import java.util.Date;

@ApiIgnore
@RequiredArgsConstructor
public abstract class BaseUtilityController {

    @Autowired
    private UserService userService;

    private final SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy HH:mm a");

    @Value("${server.host}")
    private String host;
    @Value("${server.websocket.protocol}")
    private String protocol;
    @Value("${server.servlet.contextPath}")
    private String context;

    protected final String pageTitle;

    @ModelAttribute("title")
    public String getPageTitle() {
        return pageTitle;
    }

    @ModelAttribute("login")
    public String getLogin() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }


    @GetMapping
    protected abstract Object getHome(Model model);

    public String pageWithContent(String content, Model model) {
        model.addAttribute("content", content);
        return "template";
    }

    @ModelAttribute("url")
    protected String getPageUrl() {
        return getClass().getAnnotation(RequestMapping.class).value()[0] + '/';
    }

    protected Object defaultPage(Model model) {
        return new RedirectView(getPageUrl(), false);
    }

    protected Object redirect(String url, Model model, RedirectAttributes redirectAttributes) {
        if (model != null && redirectAttributes != null)
            model.asMap().forEach(redirectAttributes::addFlashAttribute);
        return new RedirectView(url, false);
    }


    @ModelAttribute("version")
    public String getVersion() {
        return "0.1";
    }

    @ModelAttribute("serverTime")
    public String getServerTime() {
        return df.format(new Date());
    }


    @ModelAttribute("user")
    public UserEntity getUser() {
        return userService.getMember();
    }

    @ModelAttribute("chatAddress")
    public String getChatAddress() {
        return protocol + "://" + host + context + "/chat";
    }

}
