package kz.ajs.spring_boot_social_network.controllers;

import kz.ajs.spring_boot_social_network.db.DBManager;
import kz.ajs.spring_boot_social_network.db.Items;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;

@Controller
public class HomeController {

    @GetMapping(value = "/")
    public String index(Model model){
        ArrayList<Items> items = DBManager.getItems();
        model.addAttribute("goods", items);

        return "index";
    }

    @GetMapping(value = "/about")
    public String about(){
        return "about";
    }

    @PostMapping(value = "/additem")
    public String addItem(@RequestParam(name = "item_name", defaultValue="no name") String name,
                          @RequestParam(name = "item_price", defaultValue = "0") int price){
        DBManager.addItem(new Items(null, name, price));

        return "redirect:/";
    }

    @GetMapping(value = "/details/{item_id}")
    public String details(Model model, @PathVariable(name = "item_id") Long id){
        Items item = DBManager.getItem(id);
        model.addAttribute("item", item);
        return "details";
    }

}
