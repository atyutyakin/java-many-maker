package pro.alxerxc.menuMaker.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.MultiValueMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import pro.alxerxc.menuMaker.entity.Product;
import pro.alxerxc.menuMaker.service.CategoryService;
import pro.alxerxc.menuMaker.service.ProductService;

import javax.validation.Valid;

@Controller
@RequestMapping("/products")
public class ProductController {

    private GenericCrudController<Product, Long> crudController;

    @Autowired
    private CategoryService categoryService;

    public ProductController(ProductService productService) {
        this.crudController = new GenericCrudController<>(productService, "product", "products");
    }

    @GetMapping(value = {"/all", ""})
    public String showAllProducts(
                @RequestParam MultiValueMap<String, String> allRequestParams,
                Model model) {
        return crudController.showAllEntities(allRequestParams, model);
    }

    @GetMapping("/add")
    public String showCreateForm(Product product, BindingResult result,  Model model) {
        model.addAttribute("allCategories", categoryService.findAll());
        return crudController.showNewEntityForm(product, result, model);
    }

    @GetMapping("/{id}")
    public String showProductDetails(@PathVariable("id") long id, Model model) {
        return crudController.showDetailsForm(id, model);
    }

    @GetMapping("/edit/{id}")
    public String showUpdateForm(@PathVariable("id") long id, Model model) {
        model.addAttribute("allCategories", categoryService.findAll());
        return crudController.showUpdateForm(id, model);
    }

    @PostMapping("/add")
    public String addProduct(@Valid Product product, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("allCategories", categoryService.findAll());
        }
        return crudController.addNewEntity(product, result, model);
    }

    @PostMapping("/update/{id}")
    public String updateProduct(@PathVariable("id") long id, @Valid Product product, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("allCategories", categoryService.findAll());
        }
        return crudController.updateExistingEntity(id, product, result, model);
    }

    @GetMapping("/delete/{id}")
    public String deleteProduct(@PathVariable("id") long id, Model model) {
        return crudController.deleteEntity(id, model);
    }

}
