package kz.ajs.spring_boot_social_network.db;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Items {

    private Long id;
    private String name;
    private int price;

}
