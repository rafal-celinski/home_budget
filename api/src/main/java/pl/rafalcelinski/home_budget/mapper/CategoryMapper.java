package pl.rafalcelinski.home_budget.mapper;

import org.springframework.stereotype.Component;
import pl.rafalcelinski.home_budget.dto.CategoryDTO;
import pl.rafalcelinski.home_budget.entity.Category;
import pl.rafalcelinski.home_budget.entity.User;

@Component
public class CategoryMapper {
    public CategoryDTO toDTO(Category category) {
        if (category == null) {
            return null;
        }

        CategoryDTO categoryDTO = new CategoryDTO();
        categoryDTO.setId(category.getId());
        categoryDTO.setName(category.getName());

        return categoryDTO;
    }

    public Category toEntity(CategoryDTO categoryDTO, User user) {
        if (categoryDTO == null) {
            return null;
        }

        Category category = new Category();
        category.setId(categoryDTO.getId());
        category.setName(categoryDTO.getName());
        category.setUser(user);

        return category;
    }
}
