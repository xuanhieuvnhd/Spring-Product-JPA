package com.controller;

import com.model.Category;
import com.model.Product;
import com.model.ProductForm;
import com.service.category.ICategoryService;
import com.service.product.IProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.io.File;
import java.io.IOException;
import java.util.List;


@Controller
@RequestMapping("products")
public class ProductController {
    @Autowired
    private IProductService productService;
    @Autowired
    private ICategoryService categoryService;
    @Value("C:\\Users\\Administrator\\Desktop\\image\\")
    private String fileUpload;

    @ModelAttribute(name = "categories")
    private List<Category> categories() {
        return categoryService.findAll();
    }

    @GetMapping
    private ModelAndView showAllProduct(String name) {
        if (name == null) {
            List<Product> products = this.productService.findAll();
            ModelAndView modelAndView = new ModelAndView("/product/list");
            modelAndView.addObject("products", products);
            return modelAndView;
        } else {
            List<Product> products = this.productService.findByName(name);
            ModelAndView modelAndView = new ModelAndView("/product/list");
            modelAndView.addObject("products", products);
            return modelAndView;
        }
    }

    @GetMapping("/create")
    private ModelAndView showCreateForm() {
        ProductForm productForm = new ProductForm();
        ModelAndView modelAndView = new ModelAndView("/product/create");
        modelAndView.addObject("productForm", productForm);
        return modelAndView;
    }

    @PostMapping("/create")
    private ModelAndView createProduct(@ModelAttribute ProductForm productForm) {
        MultipartFile imageFile = productForm.getImage();
        String fileName = imageFile.getOriginalFilename();
        long currentTime = System.currentTimeMillis();
        fileName = currentTime + fileName;
        try {
            FileCopyUtils.copy(imageFile.getBytes(), new File(fileUpload + fileName));
        } catch (IOException e) {
            e.printStackTrace();
        }
        Product newProduct = new Product(productForm.getName(), productForm.getPrice(), productForm.getQuantity(), productForm.getDescription(), fileName, productForm.getCategory());
        productService.save(newProduct);
        ModelAndView modelAndView = new ModelAndView("redirect:/products");
        return modelAndView;
    }
    @GetMapping("/edit/{id}")
    private ModelAndView showFormEdit(@PathVariable Long id){
        Product oldProduct = this.productService.findById(id);
        ModelAndView modelAndView = new ModelAndView("/product/edit");
        modelAndView.addObject("product",oldProduct);
        return modelAndView;
    }
    @PostMapping("/edit/{id}")
    private ModelAndView editProduct(@PathVariable Long id, @ModelAttribute ProductForm productForm){
        Product oldProduct = this.productService.findById(id);
        MultipartFile imageFile = productForm.getImage();
        String image;
        if (imageFile.getSize()==0){
            image = oldProduct.getImage();
        }else {
            String fileName = imageFile.getOriginalFilename();
            long currentTime = System.currentTimeMillis();
            fileName = currentTime + fileName;
            image = fileName;
            try {
                FileCopyUtils.copy(imageFile.getBytes(), new File(fileUpload + fileName));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        Product newProduct = new Product(productForm.getId(),productForm.getName(),productForm.getPrice(),productForm.getQuantity(),productForm.getDescription(),image,productForm.getCategory());
        productService.save(newProduct);
        ModelAndView modelAndView = new ModelAndView("redirect:/products");
        return modelAndView;
    }
    @GetMapping("/delete/{id}")
    private ModelAndView showFormDelete(@PathVariable Long id){
        Product product = this.productService.findById(id);
        ModelAndView modelAndView = new ModelAndView("/product/delete");
        modelAndView.addObject("product",product);
        return modelAndView;
    }
    @PostMapping("/delete/{id}")
    private  ModelAndView deleteProduct(@PathVariable Long id){
        Product product = this.productService.findById(id);
        File file = new File(fileUpload + product.getImage());
        if (file.exists()){
            file.delete();
        }
        this.productService.delete(id);
        ModelAndView modelAndView = new ModelAndView("redirect:/products");
        return modelAndView;
    }
    @GetMapping("/view/{id}")
    private ModelAndView showProductDetails(@PathVariable Long id){
        Product product = this.productService.findById(id);
        ModelAndView modelAndView = new ModelAndView("/product/view");
        modelAndView.addObject("product",product);
        return modelAndView;
    }

}
