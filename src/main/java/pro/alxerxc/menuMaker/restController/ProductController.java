package pro.alxerxc.menuMaker.restController;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pro.alxerxc.menuMaker.entity.Product;
import pro.alxerxc.menuMaker.service.ProductService;

import java.util.List;

@RestController
@RequestMapping("/api/product")
public class ProductController {
    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("")
    public ResponseEntity<List<Product>> findAll() {
        System.out.println("findAll() called");
        return ResponseEntity.ok(productService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Product> findById(@PathVariable("id") Long id) {
        return ResponseEntity.ok(productService.findById(id));
    }

    @PostMapping("")
    public ResponseEntity<Product> add(@RequestBody Product productToAdd) {
        System.out.println("add() called");
        return ResponseEntity.ok(productService.add(productToAdd));
    }

    @PutMapping("")
    public ResponseEntity<Product> update(@RequestBody Product productToUpdate) {
        return ResponseEntity.ok(productService.update(productToUpdate));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable("id") Long id) {
        productService.deleteById(id);
        return ResponseEntity.ok("Deleted successfully");
    }
}
