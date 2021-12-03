package kz.ajs.spring_boot_social_network.repositories;

import kz.ajs.spring_boot_social_network.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;


@Repository
@Transactional
public interface UserRepository extends JpaRepository<User, Long> {

    User findByEmail(String email);

}
