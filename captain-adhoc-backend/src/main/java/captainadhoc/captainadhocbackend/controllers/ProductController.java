package captainadhoc.captainadhocbackend.controllers;

import captainadhoc.captainadhocbackend.domain.Product;
import captainadhoc.captainadhocbackend.services.interfaces.IProductService;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;

@Setter
@RestController
@RequestMapping("/products")
public class ProductController {

    @Autowired
    private IProductService productService;

    @GetMapping
    public ArrayList<Product> getAllProducts() {
        return productService.findAllProducts();
    }

    @PostMapping("/add")
    public void addProduct(@RequestBody Product product) { }

    @PutMapping("/modify/quantity")
    public void modifyQuantity(
            @RequestParam(value = "quantity") int productQuantity,
            @RequestParam(value = "idProduct") Long idProduct) {

        if (productQuantity < 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        productService.modifyQuantity(idProduct, productQuantity);
    }

}
