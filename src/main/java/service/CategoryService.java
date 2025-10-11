package service;

import dao.CategoryDao;
import model.Category;

import java.util.List;
import java.util.Set;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

public class CategoryService {
    private static final Logger LOGGER = Logger.getLogger(CategoryService.class.getName());
    private static final Validator validator;

    static {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    private final CategoryDao categoryDao = new CategoryDao();

    public List<Category> getAllCategories() {
        return categoryDao.findAll();
    }

    public Category getCategoryById(Long id) {
        Category category = categoryDao.findById(id);
        if (category == null) {
            throw new RuntimeException("Category not found with ID: " + id);
        }
        return category;
    }

    public void createCategory(Category category) {
        validateCategory(category);
        categoryDao.create(category);
    }

    public void updateCategory(Category category) {
        validateCategory(category);
        categoryDao.update(category);
    }

    public void deleteCategory(Long id) {
        categoryDao.delete(id);
    }

    public boolean isCategoryNameExists(String name) {
        return categoryDao.isCategoryNameExists(name);
    }

    private void validateCategory(Category category) {
        Set<ConstraintViolation<Category>> violations = validator.validate(category);
        if (!violations.isEmpty()) {
            String errorMessage = violations.stream()
                    .map(v -> v.getPropertyPath() + ": " + v.getMessage())
                    .collect(Collectors.joining(", "));
            throw new RuntimeException("Validation failed: " + errorMessage);
        }
    }
}