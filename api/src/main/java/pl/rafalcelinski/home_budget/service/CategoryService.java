package pl.rafalcelinski.home_budget.service;

import org.springframework.stereotype.Service;
import pl.rafalcelinski.home_budget.dto.CategoryDTO;
import pl.rafalcelinski.home_budget.entity.Category;
import pl.rafalcelinski.home_budget.entity.User;
import pl.rafalcelinski.home_budget.exception.AccessDeniedException;
import pl.rafalcelinski.home_budget.exception.CategoryNotFoundException;
import pl.rafalcelinski.home_budget.exception.UserNotFoundException;
import pl.rafalcelinski.home_budget.mapper.CategoryMapper;
import pl.rafalcelinski.home_budget.repository.CategoryRepository;
import pl.rafalcelinski.home_budget.repository.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryService {
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;
    private final CategoryMapper categoryMapper;

    public CategoryService(CategoryRepository categoryRepository, UserRepository userRepository, CategoryMapper categoryMapper) {
        this.categoryRepository = categoryRepository;
        this.userRepository = userRepository;
        this.categoryMapper = categoryMapper;
    }

    public List<CategoryDTO> getCategories(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("User not found"));

        return categoryRepository.findByUser(user)
                .stream()
                .map(categoryMapper::toDTO)
                .collect(Collectors.toList());
    }

    public CategoryDTO addCategory(CategoryDTO categoryDTO, Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("User not found"));

        Category category = categoryMapper.toEntity(categoryDTO, user);
        category = categoryRepository.save(category);
        return categoryMapper.toDTO(category);
    }

    public void deleteCategoryById(Long categoryId, Long userId) {
        Category category = categoryRepository.findById(categoryId).orElseThrow(() -> new CategoryNotFoundException("Category not found"));
        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("User not found"));

        if (!category.getUser().equals(user)) throw new AccessDeniedException("This user does not have permission to delete this category");

        categoryRepository.delete(category);
    }

    public CategoryDTO updateCategory(Long categoryId, CategoryDTO categoryDTO, Long userId) {
        Category category = categoryRepository.findById(categoryId).orElseThrow(() -> new CategoryNotFoundException("Category not found"));
        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("User not found"));

        if (!category.getUser().equals(user)) throw new AccessDeniedException("This user does not have permission to update this category");

        categoryDTO.setId(categoryId);

        Category updatedCategory = categoryMapper.toEntity(categoryDTO, user);
        updatedCategory = categoryRepository.save(updatedCategory);
        return categoryMapper.toDTO(updatedCategory);
    }
}
