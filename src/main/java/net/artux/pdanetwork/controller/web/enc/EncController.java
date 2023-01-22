package net.artux.pdanetwork.controller.web.enc;

import lombok.RequiredArgsConstructor;
import net.artux.pdanetwork.entity.items.BaseItemEntity;
import net.artux.pdanetwork.entity.items.ItemType;
import net.artux.pdanetwork.models.items.ItemDto;
import net.artux.pdanetwork.service.items.BaseItemService;
import net.artux.pdanetwork.service.items.ItemService;
import org.hibernate.Hibernate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/enc")
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
        BaseItemEntity baseItemEntity = baseItemService.getBaseItem(id);
        Hibernate.initialize(baseItemEntity);
        model.addAttribute("base", baseItemEntity);

        return "enc/item";
    }

}
