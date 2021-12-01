package kz.ajs.spring_boot_social_network.services.impl;

import kz.ajs.spring_boot_social_network.entities.Categories;
import kz.ajs.spring_boot_social_network.entities.Countries;
import kz.ajs.spring_boot_social_network.entities.ShopItems;
import kz.ajs.spring_boot_social_network.repositories.CategoryRepository;
import kz.ajs.spring_boot_social_network.repositories.CountryRepository;
import kz.ajs.spring_boot_social_network.repositories.ShopItemRepository;
import kz.ajs.spring_boot_social_network.services.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ItemServiceImpl implements ItemService {

    @Autowired
    protected ShopItemRepository shopItemRepository;

    @Autowired
    private CountryRepository countryRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Override
    public ShopItems addItem(ShopItems item) {
        return shopItemRepository.save(item);
    }

    @Override
    public List<ShopItems> getAllItems() {
        return shopItemRepository.findByAmountGreaterThanOrderByPriceDesc(0);
    }

    @Override
    public ShopItems getItem(Long id) {
        return shopItemRepository.findByIdAndAmountGreaterThan(id, 0);
    }

    @Override
    public void deleteItem(ShopItems item) {
        shopItemRepository.delete(item);
    }

    @Override
    public ShopItems saveItem(ShopItems item) {
        return shopItemRepository.save(item);
    }

    @Override
    public List<Countries> getAllCountries() {
        return countryRepository.findAll();
    }

    @Override
    public Countries addCountry(Countries country) {
        return countryRepository.save(country);
    }

    @Override
    public Countries saveCountry(Countries country) {
        return countryRepository.save(country);
    }

    @Override
    public Countries getCountry(Long id) {
        return countryRepository.getById(id);
    }

    @Override
    public List<Categories> getAllCategories() {
        return categoryRepository.findAll();
    }

    @Override
    public Categories addCategory(Categories category) {
        return categoryRepository.save(category);
    }

    @Override
    public Categories saveCategory(Categories category) {
        return categoryRepository.save(category);
    }

    @Override
    public Categories getCategory(Long id) {
        return categoryRepository.getById(id);
    }

}
