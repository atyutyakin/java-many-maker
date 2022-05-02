package pro.alxerxc.menuMaker.controller;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import pro.alxerxc.menuMaker.entity.Product;
import pro.alxerxc.menuMaker.service.ProductService;
import pro.alxerxc.menuMaker.support.Pagination;

import javax.validation.Valid;

@Controller
@ConfigurationProperties(prefix = "pro.alxerxc.menu-maker.controller")
@RequestMapping("/products")
public class ProductController {
    private static final String REDIRECT_TO_INDEX_VIEW = "redirect:/products/all";
    private static final String INDEX_VIEW = "/product/index";
    private static final String EDIT_VIEW = "/product/edit";

    private static final String VIEW_VIEW = "/product/view";

    @Getter
    @Setter
    private int defaultPageSize = 15;

    @Getter
    @Setter
    private int maxPaginationLinks = 10;

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping(value = {"/all", ""})
    public String showAllProducts(
                @RequestParam(value = "search", required = false, defaultValue = "") String searchPattern,
                @RequestParam(value = "page", required = false, defaultValue = "0") int pageIndex,
                @RequestParam(value = "size", required = false, defaultValue = "0") int size,
                @RequestParam(value = "sort", required = false, defaultValue = "name,asc") String[] sort,
                Model model) {
        Page<Product> productsPage = productService.getProductsPage(searchPattern, pageIndex,
                actualPageSize(size), Pagination.sort(sort));
        model.addAttribute("products", productsPage.getContent());
        model.addAttribute("pagination", Pagination.of(productsPage, searchPattern, maxPaginationLinks));
        return INDEX_VIEW;
    }

    @GetMapping("/add")
    public String showCreateForm(Product user, Model model) {
        setIsNew(model);
        return EDIT_VIEW;
    }

    @GetMapping("/{id}")
    public String showProductDetails(@PathVariable("id") long id, Model model) {
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

    private int actualPageSize(int size) {
        return (size != 0) ? size : defaultPageSize;
    }
}
