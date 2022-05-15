package pro.alxerxc.menuMaker.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.MultiValueMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import pro.alxerxc.menuMaker.entity.Category;
import pro.alxerxc.menuMaker.entity.Product;
import pro.alxerxc.menuMaker.service.CategoryService;
import pro.alxerxc.menuMaker.service.ProductService;

import javax.validation.Valid;

@Controller
@RequestMapping("/categories")
public class CategoryController {

    private GenericCrudController<Category, Long> crudController;

    public CategoryController(CategoryService categoryService) {
        this.crudController = new GenericCrudController<>(categoryService, "category", "categories");
    }

    @GetMapping(value = {"/all", ""})
    public String showAllCategories(
                @RequestParam MultiValueMap<String, String> allRequestParams,
                Model model) {
        return crudController.showAllEntities(allRequestParams, model);
    }

    @GetMapping("/add")
    public String showCreateForm(Category category, BindingResult result,  Model model) {
        return crudController.showNewEntityForm(category, result, model);
    }

    @GetMapping("/{id}")
    public String showCategoryDetails(@PathVariable("id") long id, Model model) {
        return crudController.showDetailsForm(id, model);
    }

    @GetMapping("/edit/{id}")
    public String showUpdateForm(@PathVariable("id") long id, Model model) {
        return crudController.showUpdateForm(id, model);
    }

    @PostMapping("/add")
    public String addCategory(@Valid Category category, BindingResult result, Model model) {
        return crudController.addNewEntity(category, result, model);
    }

    @PostMapping("/update/{id}")
    public String updateCategory(@PathVariable("id") long id, @Valid Category category, BindingResult result, Model model) {
        return crudController.updateExistingEntity(id, category, result, model);
    }

    @GetMapping("/delete/{id}")
    public String deleteCategory(@PathVariable("id") long id, Model model) {
        return crudController.deleteEntity(id, model);
    }

}
