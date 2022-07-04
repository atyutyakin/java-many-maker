package pro.alxerxc.menuMaker.service;

import org.springframework.stereotype.Service;
import pro.alxerxc.menuMaker.entity.Product;
import pro.alxerxc.menuMaker.repository.ProductRepository;

import java.time.Instant;

@Service
public class ProductService extends GenericCrudService<Product, Long> {

    public ProductService(ProductRepository repository) {
        super(repository);
    }

    @Override
    public Product add(Product entityToAdd) {
        entityToAdd.setCreatedAt(Instant.now());
        return super.add(entityToAdd);
    }

    @Override
    public Product update(Product entityToUpdate) {
        entityToUpdate.setModifiedAt(Instant.now());
        return super.update(entityToUpdate);
    }

    @Override
    protected void doBeforeSave(Product entityToSave) {
        if (entityToSave.getCategory() != null && entityToSave.getCategory().getId() == null) {
            entityToSave.setCategory(null);
        }
    }
}
