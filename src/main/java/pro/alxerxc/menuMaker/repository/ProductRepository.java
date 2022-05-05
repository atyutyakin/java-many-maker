package pro.alxerxc.menuMaker.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pro.alxerxc.menuMaker.entity.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long>, GeneralPageableRepository<Product> {

}
