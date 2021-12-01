package kz.ajs.spring_boot_social_network.repositories;

import kz.ajs.spring_boot_social_network.entities.Categories;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;


@Repository
@Transactional
public interface CategoryRepository extends JpaRepository<Categories, Long> {
}
