package pl.rafalcelinski.home_budget.controller;

import jakarta.validation.groups.Default;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import pl.rafalcelinski.home_budget.dto.CategoryDTO;
import pl.rafalcelinski.home_budget.dto.validation.groups.OnCreate;
import pl.rafalcelinski.home_budget.service.AuthorizationService;
import pl.rafalcelinski.home_budget.service.CategoryService;

import java.util.List;

@RestController
@RequestMapping("/categories")
public class CategoryController {
    private final CategoryService categoryService;
    private final AuthorizationService authorizationService;

    public CategoryController(CategoryService categoryService, AuthorizationService authorizationService) {
        this.categoryService = categoryService;
        this.authorizationService = authorizationService;
    }

    @GetMapping
    public ResponseEntity<List<CategoryDTO>> getCategories(@RequestHeader("Authorization") String authorizationHeader) {

        Long userId = authorizationService.validAuthorizationAndExtractUserID(authorizationHeader);
        List<CategoryDTO> categories = categoryService.getCategories(userId);

        return ResponseEntity.ok(categories);
    }

    @PostMapping
    public ResponseEntity<CategoryDTO> addCategory(@RequestBody @Validated({OnCreate.class, Default.class}) CategoryDTO categoryDTO,
                                         @RequestHeader("Authorization") String authorizationHeader) {

        Long userId = authorizationService.validAuthorizationAndExtractUserID(authorizationHeader);
        CategoryDTO newCategory = categoryService.addCategory(categoryDTO, userId);

        return ResponseEntity.ok(newCategory);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable Long id,
                                            @RequestHeader("Authorization") String authorizationHeader) {

        Long userId = authorizationService.validAuthorizationAndExtractUserID(authorizationHeader);
        categoryService.deleteCategoryById(id, userId);

        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<CategoryDTO> updateCategory(@PathVariable Long id,
                                            @RequestBody @Validated({OnCreate.class, Default.class}) CategoryDTO categoryDTO,
                                            @RequestHeader("Authorization") String authorizationHeader) {

        Long userId = authorizationService.validAuthorizationAndExtractUserID(authorizationHeader);
        CategoryDTO newCategory = categoryService.updateCategory(id, categoryDTO, userId);

        return ResponseEntity.ok(newCategory);
    }
}
