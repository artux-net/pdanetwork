package net.artux.pdanetwork.controller;

import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import net.artux.pdanetwork.models.profile.items.Armor;
import net.artux.pdanetwork.models.profile.items.Item;
import net.artux.pdanetwork.models.profile.items.Weapon;
import net.artux.pdanetwork.utills.ItemsManager;
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

  @GetMapping("/{type}")
  public String getTypeList(Model model, @PathVariable Integer type){
    model.addAttribute("items", types.getEncTypeList(type));
    return "enc/list";
  }

  @GetMapping("/{type}/{id}")
  public String getItemPage(Model model, @PathVariable Integer type, @PathVariable Integer id){
    Item item = types.getItem(type,id);
    model.addAttribute("enc", types.getEncItem(type,id));
    model.addAttribute("item", item);

    if (type == 4) {
      Armor armor = (Armor) item;

      return "enc/armor";
    } else if(type == 1 || type == 0){
      Weapon weapon = (Weapon) item;
      model.addAttribute("precision", weapon.getPrecision());
      model.addAttribute("damage", weapon.getDamage());
      model.addAttribute("speed", weapon.getSpeed());
      model.addAttribute("bullet", types.getItem(2, weapon.getBullet_id()));
      return "enc/weapon";
    }
    return "enc/weapon";
  }



}
