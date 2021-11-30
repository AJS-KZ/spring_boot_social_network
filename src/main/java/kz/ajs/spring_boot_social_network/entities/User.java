package kz.ajs.spring_boot_social_network.entities;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name="users")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "fullName", length = 500)
    private String fullName;

    @Column(name = "avatar", length = 1000)
    private String avatar;

    @Column(name = "followed")
    private boolean followed;

    @Column(name = "bio")
    private String bio;

}
