package pro.alxerxc.menuMaker.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pro.alxerxc.menuMaker.entity.Category;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long>, GenericPageableRepository<Category> {
}
