package pro.alxerxc.menuMaker.service;

import org.springframework.stereotype.Service;
import pro.alxerxc.menuMaker.entity.Product;
import pro.alxerxc.menuMaker.repository.ProductRepository;

@Service
public class ProductService extends GenericCrudService<Product, Long> {

    public ProductService(ProductRepository repository) {
        super(repository);
    }

}
