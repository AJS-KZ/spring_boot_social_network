package kz.ajs.spring_boot_social_network.services;

import kz.ajs.spring_boot_social_network.entities.Categories;
import kz.ajs.spring_boot_social_network.entities.Countries;
import kz.ajs.spring_boot_social_network.entities.ShopItems;

import java.util.List;

public interface ItemService {

    ShopItems addItem(ShopItems item);
    List<ShopItems> getAllItems();
    ShopItems getItem(Long id);
    void deleteItem(ShopItems item);
    ShopItems saveItem(ShopItems item);

    List<Countries> getAllCountries();
    Countries addCountry(Countries country);
    Countries saveCountry(Countries country);
    Countries getCountry(Long id);

    List<Categories> getAllCategories();
    Categories addCategory(Categories category);
    Categories saveCategory(Categories category);
    Categories getCategory(Long id);

}
