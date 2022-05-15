package pro.alxerxc.menuMaker.service;

import org.springframework.stereotype.Service;
import pro.alxerxc.menuMaker.entity.Category;
import pro.alxerxc.menuMaker.repository.CategoryRepository;

@Service
public class CategoryService extends GenericCrudService<Category, Long> {

    public CategoryService(CategoryRepository repository) {
        super(repository);
    }

}
