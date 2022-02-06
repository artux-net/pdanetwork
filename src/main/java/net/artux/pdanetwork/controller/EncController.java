package net.artux.pdanetwork.controller;

import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import net.artux.pdanetwork.models.profile.items.ItemEntity;
import net.artux.pdanetwork.models.profile.items.WeaponEntity;
import net.artux.pdanetwork.service.ItemsManager;
import net.artux.pdanetwork.service.files.Types;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/enc")
@Api(tags = "Энциклопедия")
@RequiredArgsConstructor
public class EncController {

  private final Types types;
  private final ItemsManager itemsManager;

  @GetMapping
  public String getMainPage(Model model){
    model.addAttribute("types", types.getItemTypes());
    return "enc/main";
  }

  @GetMapping("/{type}")
  public String getTypeList(Model model, @PathVariable Integer type){
    model.addAttribute("items", types.getEncTypeList(type));
    return "enc/list";
  }

  @GetMapping("/{type}/{id}")
  public String getItemPage(Model model, @PathVariable Integer type, @PathVariable Integer id){
    ItemEntity itemEntity = types.getItem(type,id);
    model.addAttribute("enc", types.getEncItem(type,id));
    model.addAttribute("item", itemEntity);

    if (type == 4) {
      return "enc/armor";
    } else if(type == 1 || type == 0){
      WeaponEntity weaponEntity = (WeaponEntity) itemEntity;
      model.addAttribute("precision", weaponEntity.getPrecision());
      model.addAttribute("damage", weaponEntity.getDamage());
      model.addAttribute("speed", weaponEntity.getSpeed());
      model.addAttribute("bullet", types.getItem(2, weaponEntity.getBullet_id()));
      return "enc/weapon";
    }
    return "enc/weapon";
  }



}
