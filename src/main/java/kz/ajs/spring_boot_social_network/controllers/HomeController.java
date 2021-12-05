package kz.ajs.spring_boot_social_network.controllers;

import kz.ajs.spring_boot_social_network.entities.*;
import kz.ajs.spring_boot_social_network.services.ItemService;
import kz.ajs.spring_boot_social_network.services.UserService;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.extras.springsecurity5.auth.Authorization;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Controller
public class HomeController {

    @Autowired
    private ItemService itemService;

    @Autowired
    private UserService userService;

    @Value("${file.avatar.viewPath}")
    private String viewPath;
    @Value("${file.avatar.uploadPath}")
    private String uploadPath;
    @Value("${file.avatar.defaultAva}")
    private String defaultAva;

    @GetMapping(value = "/")
    public String index(Model model){
        model.addAttribute("currentUser", getUserData());

        List<ShopItems> items = itemService.getAllItems();
        model.addAttribute("goods", items);

        List<Countries> countries = itemService.getAllCountries();
        model.addAttribute("countries", countries);

        return "index";
    }

    @GetMapping(value = "/about")
    public String about(Model model){
        model.addAttribute("currentUser", getUserData());
        return "about";
    }

    @PostMapping(value = "/additem")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MODERATOR')")
    public String addItem(@RequestParam(name = "item_name", defaultValue="no name") String name,
                          @RequestParam(name = "item_price", defaultValue = "0") int price,
                          @RequestParam(name = "item_amount", defaultValue = "0") int amount,
                          @RequestParam(name = "country_id", defaultValue = "0") Long country_id){

        Countries country = itemService.getCountry(country_id);
        if(country!=null){
            ShopItems new_item = new ShopItems();
            new_item.setName(name);
            new_item.setPrice(price);
            new_item.setAmount(amount);
            new_item.setCountry(country);
            itemService.addItem(new_item);
        }

        return "redirect:/";
    }

    @GetMapping(value = "/details/{item_id}")
    public String details(Model model, @PathVariable(name = "item_id") Long id){
        model.addAttribute("currentUser", getUserData());

        ShopItems item = itemService.getItem(id);
        model.addAttribute("item", item);
        List<Countries> countries = itemService.getAllCountries();
        model.addAttribute("countries", countries);
        List<Categories> allCategories = itemService.getAllCategories();
        model.addAttribute("categories", allCategories);

        return "details";
    }

    @GetMapping(value = "/edititem/{item_id}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MODERATOR')")
    public String editItem(Model model, @PathVariable(name = "item_id") Long id){
        model.addAttribute("currentUser", getUserData());

        ShopItems item = itemService.getItem(id);
        model.addAttribute("item", item);

        List<Countries> countries = itemService.getAllCountries();
        model.addAttribute("countries", countries);

        List<Categories> allCategories = itemService.getAllCategories();
        allCategories.removeAll(item.getCategory());
        model.addAttribute("categories", allCategories);

        return "edititem";
    }

    @PostMapping(value = "/saveitem")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MODERATOR')")
    public String saveItem(@RequestParam(name = "item_id", defaultValue="0") Long id,
                           @RequestParam(name = "item_name", defaultValue="no name") String name,
                           @RequestParam(name = "item_price", defaultValue = "0") int price,
                           @RequestParam(name = "item_amount", defaultValue = "0") int amount,
                           @RequestParam(name = "country_id", defaultValue = "0") Long country_id){

        ShopItems item = itemService.getItem(id);
        if(item!=null){
            item.setName(name);
            item.setPrice(price);
            item.setAmount(amount);

        }

        Countries country = itemService.getCountry(country_id);
        if(country!=null){
            item.setCountry(country);
        }

        itemService.saveItem(item);

        return "redirect:/edititem/"+id;
    }

    @PostMapping(value = "/deleteitem")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MODERATOR')")
    public String deleteItem(@RequestParam(name = "item_id", defaultValue = "0") Long id){

        ShopItems item = itemService.getItem(id);
        if(item!=null) {
            itemService.deleteItem(item);
        }

        return "redirect:/";
    }

    @PostMapping(value = "/assigncategory")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MODERATOR')")
    public String assignCategory(@RequestParam(name = "item_id", defaultValue = "0") Long itemId,
                                 @RequestParam(name = "category_id", defaultValue = "0")Long categoryId){

        Categories cat = itemService.getCategory(categoryId);
        if(cat!=null){
            ShopItems item = itemService.getItem(itemId);
            if(item!=null){
                List<Categories> categories = item.getCategory();
                if(categories==null){
                    categories = new ArrayList<Categories>();
                }
                categories.add(cat);
                itemService.saveItem(item);

                return "redirect:/edititem/"+item.getId()+"#categoriesDiv";
            }
        }

        return "redirect:/";

    }

    @PostMapping(value = "/unassigncategory")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MODERATOR')")
    public String unAssignCategory(@RequestParam(name = "item_id", defaultValue = "0") Long itemId,
                                 @RequestParam(name = "category_id", defaultValue = "0")Long categoryId){

        Categories cat = itemService.getCategory(categoryId);
        if(cat!=null){
            ShopItems item = itemService.getItem(itemId);
            if(item!=null){
                List<Categories> categories = item.getCategory();
                if(categories==null){
                    categories = new ArrayList<Categories>();
                }
                categories.remove(cat);
                itemService.saveItem(item);

                return "redirect:/edititem/"+item.getId()+"#categoriesDiv";
            }
        }

        return "redirect:/";

    }

    @GetMapping(value = "/403")
    public String accessDenied(Model model){
        model.addAttribute("currentUser", getUserData());
        return "403";
    }

    @GetMapping(value = "/login")
    public String login(Model model){
        model.addAttribute("currentUser", getUserData());
        return "login";
    }

    @GetMapping(value = "/profile")
    @PreAuthorize("isAuthenticated()")
    public String profile(Model model){

        model.addAttribute("currentUser", getUserData());

        return "profile";
    }

    @GetMapping(value = "/register")
    @PreAuthorize("isAnonymous()")
    public String register(Model model){

        model.addAttribute("currentUser", getUserData());

        return "register";
    }

    @PostMapping(value = "/register")
    public String toRegister(@RequestParam(name = "user_email") String email,
                             @RequestParam(name = "user_password") String password,
                             @RequestParam(name = "user_re_password") String re_password,
                             @RequestParam(name = "user_full_name") String full_name){

        if(password.equals(re_password)){

            User new_user = new User();
            new_user.setEmail(email);
            new_user.setPassword(password);
            new_user.setFullName(full_name);
            if(userService.createUser(new_user)!=null){
                return "redirect:/";
            }

        }

        return "403";
    }

    @GetMapping(value = "/additem")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MODERATOR')")
    public String addItem(Model model){

        model.addAttribute("currentUser", getUserData());
        List<Countries> countries = itemService.getAllCountries();
        model.addAttribute("countries", countries);
        List<Categories> allCategories = itemService.getAllCategories();
        model.addAttribute("categories", allCategories);

        return "additem";
    }

    @PostMapping(value = "/uploadavatar")
    @PreAuthorize("isAuthenticated()")
    public String uploadAvatar(@RequestParam(name = "user_ava") MultipartFile ava_file){

        if(ava_file.getContentType().equals("image/jpeg")||ava_file.getContentType().equals("image/png")){

            User currentUser = getUserData();
            String pic_name = DigestUtils.sha1Hex("user_ava_"+currentUser.getFullName()+"!pic");

            try{
                byte[] bytes = ava_file.getBytes();
                Path path = Paths.get(uploadPath+pic_name+".jpg");
                Files.write(path, bytes);
                currentUser.setAvatar(pic_name);
                userService.saveUser(currentUser);
                return "redirect:/profile?success";
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return "redirect:/profile?error_upload_ava";
    }

    @GetMapping(value = "/viewpics/{url}", produces = {MediaType.IMAGE_JPEG_VALUE})
    public @ResponseBody byte[] viewProfileAva(@PathVariable(name = "url") String url) throws IOException {

        String picURL = viewPath+defaultAva;

        if(url!=null&&!url.equals("null")){
            picURL = viewPath+url+".jpg";
        }

        InputStream in;

        try {
            ClassPathResource resource = new ClassPathResource(picURL);
            in = resource.getInputStream();
        }catch (Exception e){
            ClassPathResource resource = new ClassPathResource(viewPath+defaultAva);
            in = resource.getInputStream();
            e.printStackTrace();
        }

        return IOUtils.toByteArray(in);
    }

    private User getUserData(){

        Authentication authorization = SecurityContextHolder.getContext().getAuthentication();
        if(!(authorization instanceof AnonymousAuthenticationToken)){
            org.springframework.security.core.userdetails.User secUser =
                    (org.springframework.security.core.userdetails.User)authorization.getPrincipal();
            User myUser = userService.getUserByEmail(secUser.getUsername());
            return myUser;
        }

        return null;
    }

}
