package net.artux.pdanetwork.controller;

import lombok.RequiredArgsConstructor;
import net.artux.pdanetwork.models.profile.items.ItemEntity;
import net.artux.pdanetwork.models.profile.items.WeaponEntity;
import net.artux.pdanetwork.service.files.ItemProvider;
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

    private final ItemProvider itemProvider;

    @GetMapping
    public String getMainPage(Model model) {
        model.addAttribute("types", itemProvider.getItemEncTypes());
        return "enc/main";
    }

    @GetMapping("/{type}")
    public String getTypeList(Model model, @PathVariable Integer type) {
        model.addAttribute("items", itemProvider.getEncTypeList(type));
        return "enc/list";
    }

    @GetMapping("/{type}/{id}")
    public String getItemPage(Model model, @PathVariable Integer type, @PathVariable Integer id) {
        ItemEntity itemEntity = itemProvider.getItem(type, id);
        model.addAttribute("enc", itemProvider.getEncItem(type, id));
        model.addAttribute("item", itemEntity);

        if (type == 4) {
            return "enc/armor";
        } else if (type == 1 || type == 0) {
            WeaponEntity weaponEntity = (WeaponEntity) itemEntity;
            model.addAttribute("precision", weaponEntity.getPrecision());
            model.addAttribute("damage", weaponEntity.getDamage());
            model.addAttribute("speed", weaponEntity.getSpeed());
            model.addAttribute("bullet", itemProvider.getItem(2, weaponEntity.getBulletId()));
            return "enc/weapon";
        }
        return "enc/weapon";
    }


}
