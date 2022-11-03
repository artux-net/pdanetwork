package net.artux.pdanetwork.controller.admin;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Controller
@RequestMapping("/utility/logs")
public class AdminLogsController extends BaseUtilityController {

    @Value("${logging.file.name}")
    private String fileName;

    public AdminLogsController() {
        super("Логи");
    }

    @ModelAttribute("logs")
    public String getLogs() throws IOException {
        return IOUtils.toString(new FileInputStream(fileName), StandardCharsets.UTF_8);
    }

    @Override
    protected Object getHome(Model model) {
        return pageWithContent("logs", model);
    }

}
