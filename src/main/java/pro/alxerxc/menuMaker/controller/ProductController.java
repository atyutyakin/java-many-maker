package pro.alxerxc.menuMaker.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import pro.alxerxc.menuMaker.entity.Product;
import pro.alxerxc.menuMaker.entity.User;
import pro.alxerxc.menuMaker.service.ProductService;

import javax.validation.Valid;

@Controller
@RequestMapping("/products")
public class ProductController {
    private static final String REDIRECT_TO_INDEX_VIEW = "redirect:/products/index";
    private static final String INDEX_VIEW = "/product/index";
    private static final String EDIT_VIEW = "/product/edit";

    private static final String VIEW_VIEW = "/product/view";

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("")
    public String showProductsRoot(Model model) {
        return showIndex(model);
    }

    @GetMapping("/index")
    public String showIndex(Model model) {
        model.addAttribute("products", productService.findAll());
        return INDEX_VIEW;
    }

    @GetMapping("/add")
    public String showCreateForm(Product user, Model model) {
        setIsNew(model);
        return EDIT_VIEW;
    }

    @GetMapping("/{id}")
    public String showDetails(@PathVariable("id") long id, Model model) {
        model.addAttribute("product", productService.findById(id));
        System.out.println("reached this");
        return VIEW_VIEW;
    }

    @GetMapping("/edit/{id}")
    public String showUpdateForm(@PathVariable("id") long id, Model model) {
        Product product = productService.findById(id);
        model.addAttribute("product", product);
        return EDIT_VIEW;
    }

    @PostMapping("/add")
    public String addUser(@Valid Product product, BindingResult result, Model model) {
        if (result.hasErrors()) {
            setIsNew(model);
            return EDIT_VIEW;
        }
        productService.add(product);
        return REDIRECT_TO_INDEX_VIEW;
    }

    @PostMapping("/update/{id}")
    public String updateUser(@PathVariable("id") long id, @Valid Product product, BindingResult result, Model model) {
        product.setId(id);
        if (result.hasErrors()) {
            return EDIT_VIEW;
        }
        productService.update(product);
        return REDIRECT_TO_INDEX_VIEW;
    }

    @GetMapping("/delete/{id}")
    public String deleteUser(@PathVariable("id") long id, Model model) {
        productService.deleteById(id);
        return REDIRECT_TO_INDEX_VIEW;
    }

    private void setIsNew(Model model) {
        model.addAttribute("isNew", Boolean.TRUE);
    }
}
