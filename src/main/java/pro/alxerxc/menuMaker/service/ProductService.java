package pro.alxerxc.menuMaker.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import pro.alxerxc.menuMaker.entity.Product;
import pro.alxerxc.menuMaker.repository.ProductRepository;

@Service
public class ProductService extends GenericCrudService<Product, Long> {

    public ProductService(ProductRepository repository) {
        super(repository);
    }

    public Page<Product> getProductsPage(String searchString, int pageIndex, int size, Sort sort) {
        PageRequest request = PageRequest.of(pageIndex, size, sort);
        return getRepository().findAll(request);
    }

}
