package kz.ajs.spring_boot_social_network.controllers;

import kz.ajs.spring_boot_social_network.db.DBManager;
import kz.ajs.spring_boot_social_network.db.Items;
import kz.ajs.spring_boot_social_network.entities.ShopItems;
import kz.ajs.spring_boot_social_network.services.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;

@Controller
public class HomeController {

    @Autowired
    private ItemService itemService;

    @GetMapping(value = "/")
    public String index(Model model){
        List<ShopItems> items = itemService.getAllItems();
        model.addAttribute("goods", items);
        return "index";
    }

    @GetMapping(value = "/about")
    public String about(){
        return "about";
    }

    @PostMapping(value = "/additem")
    public String addItem(@RequestParam(name = "item_name", defaultValue="no name") String name,
                          @RequestParam(name = "item_price", defaultValue = "0") int price,
                          @RequestParam(name = "item_amount", defaultValue = "0") int amount){

        itemService.addItem(new ShopItems(null, name, price, amount));

        return "redirect:/";
    }

    @GetMapping(value = "/details/{item_id}")
    public String details(Model model, @PathVariable(name = "item_id") Long id){
        ShopItems item = itemService.getItem(id);
        model.addAttribute("item", item);
        return "details";
    }

    @PostMapping(value = "/saveitem")
    public String saveItem(@RequestParam(name = "item_id", defaultValue="0") Long id,
                           @RequestParam(name = "item_name", defaultValue="no name") String name,
                           @RequestParam(name = "item_price", defaultValue = "0") int price,
                           @RequestParam(name = "item_amount", defaultValue = "0") int amount){

        ShopItems item = itemService.getItem(id);
        if(item!=null){
            item.setName(name);
            item.setPrice(price);
            item.setAmount(amount);
            itemService.saveItem(item);
        }

        return "redirect:/";
    }

    @PostMapping(value = "/deleteitem")
    public String deleteItem(@RequestParam(name = "item_id", defaultValue = "0") Long id){

        ShopItems item = itemService.getItem(id);
        if(item!=null) {
            itemService.deleteItem(item);
        }

        return "redirect:/";
    }

}
