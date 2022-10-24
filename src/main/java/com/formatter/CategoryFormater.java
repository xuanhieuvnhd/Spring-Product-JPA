package com.formatter;

import com.model.Category;
import com.service.category.ICategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.Formatter;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.util.Locale;

@Component
public class CategoryFormater implements Formatter<Category> {
    private ICategoryService categoryService;

    @Autowired
    public CategoryFormater(ICategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @Override
    public Category parse(String text, Locale locale) throws ParseException {
        Category category = categoryService.findById(Long.parseLong(text));
        return category;
    }


    @Override
    public String print(Category object, Locale locale) {
        return "[" + object.getId() + ", " + object.getName() + "]";
    }
}
