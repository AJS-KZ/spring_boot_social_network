package kz.ajs.spring_boot_social_network.db;

import java.util.ArrayList;

public class DBManager {

    private static ArrayList<Items> items = new ArrayList<>();

    static {
        items.add(new Items(1L, "iPhone 11Pro", 420000));
        items.add(new Items(2L, "XiaoMi Redmi Note 9", 80000));
        items.add(new Items(3L, "Samsung Galaxy S10", 350000));
        items.add(new Items(4L, "Nokia 3110", 110000));
    }

    public static void addItem(Items item){
        long new_id = items.size()+1;
        item.setId(new_id);
        items.add(item);
    }

    public static ArrayList<Items> getItems(){
        return items;
    }

    public static Items getItem(Long id){
        for(Items it : items){
            if(it.getId()==id){
                return it;
            }
        }
        return null;
    }

}
