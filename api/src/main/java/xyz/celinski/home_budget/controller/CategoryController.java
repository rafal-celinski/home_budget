package xyz.celinski.home_budget.controller;


import jakarta.validation.groups.Default;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import xyz.celinski.home_budget.dto.CategoryDTO;
import xyz.celinski.home_budget.dto.TokenDTO;
import xyz.celinski.home_budget.dto.validation.groups.OnCreate;
import xyz.celinski.home_budget.exception.InvalidTokenException;
import xyz.celinski.home_budget.service.CategoryService;
import xyz.celinski.home_budget.service.TokenService;

import java.util.List;

@RestController
@RequestMapping("/categories")
public class CategoryController {
    private final CategoryService categoryService;
    private final TokenService tokenService;

    public CategoryController(CategoryService categoryService, TokenService tokenService) {
        this.categoryService = categoryService;
        this.tokenService = tokenService;
    }

    private Long validAuthorizationAndExtractUserID(String authorizationHeader) {
        TokenDTO tokenDTO = tokenService.authorizationHeaderToTokenDTO(authorizationHeader);
        if (!tokenService.isTokenValid(tokenDTO)) {
            throw new InvalidTokenException("Invalid token");
        }
        return tokenService.extractUserId(tokenDTO);
    }

    @GetMapping
    public ResponseEntity<?> getCategories(@RequestHeader("Authorization") String authorizationHeader) {

        Long userId = validAuthorizationAndExtractUserID(authorizationHeader);

        List<CategoryDTO> categories = categoryService.getCategories(userId);
        return ResponseEntity.ok(categories);
    }

    @PostMapping
    public ResponseEntity<?> addCategory(@RequestBody @Validated({OnCreate.class, Default.class}) CategoryDTO categoryDTO,
                                         @RequestHeader("Authorization") String authorizationHeader) {

        Long userId = validAuthorizationAndExtractUserID(authorizationHeader);
        CategoryDTO newCategory = categoryService.addCategory(categoryDTO, userId);
        return ResponseEntity.ok(newCategory);

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCategory(@PathVariable Long id,
                                            @RequestHeader("Authorization") String authorizationHeader) {

        Long userId = validAuthorizationAndExtractUserID(authorizationHeader);
        categoryService.deleteCategoryById(id, userId);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateCategory(@PathVariable Long id,
                                            @RequestBody @Validated({OnCreate.class, Default.class}) CategoryDTO categoryDTO,
                                            @RequestHeader("Authorization") String authorizationHeader) {

        Long userId = validAuthorizationAndExtractUserID(authorizationHeader);
        CategoryDTO newCategory = categoryService.updateCategory(id, categoryDTO, userId);
        return ResponseEntity.ok(newCategory);
    }



}
