package net.artux.pdanetwork.controller.enc;

import lombok.RequiredArgsConstructor;
import net.artux.pdanetwork.entity.items.ItemType;
import net.artux.pdanetwork.models.items.ItemDto;
import net.artux.pdanetwork.service.items.BaseItemService;
import net.artux.pdanetwork.service.items.ItemService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import springfox.documentation.annotations.ApiIgnore;

@Controller
@RequestMapping("/enc")
@ApiIgnore
@RequiredArgsConstructor
public class EncController {

    private final ItemService itemService;
    private final BaseItemService baseItemService;

    @GetMapping
    public String getMainPage(Model model) {
        model.addAttribute("types", ItemType.values());
        return "enc/types";
    }

    @GetMapping("/{type}")
    public String getTypeList(Model model, @PathVariable ItemType type) {
        model.addAttribute("items", baseItemService.getTypeItems(type));
        return "enc/list";
    }

    @GetMapping("/item/{id}")
    public String getItemPage(Model model, @PathVariable java.lang.Integer id) {

        ItemDto dto = itemService.getItemDto(id);
        model.addAttribute("item", dto);
        model.addAttribute("base", baseItemService.getBaseItem(id));

        return "enc/item";
    }

}
