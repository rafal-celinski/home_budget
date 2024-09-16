package pl.rafalcelinski.home_budget.dto;

import jakarta.validation.constraints.*;
import pl.rafalcelinski.home_budget.dto.validation.groups.OnCreate;
import pl.rafalcelinski.home_budget.dto.validation.groups.OnDisplay;
import pl.rafalcelinski.home_budget.dto.validation.groups.OnUpdate;


public class ExpenseDTO {
    @Null(groups = {OnCreate.class, OnUpdate.class})
    @NotNull(groups = OnDisplay.class)
    private Long Id;
    @NotNull
    @Positive
    private Double amount;
    @NotBlank
    private String description;
    @NotNull
    @Pattern(regexp = "\\d{4}-\\d{2}-\\d{2}")
    private String date;
    @NotNull
    private Long categoryId;

    public Long getId() {
        return Id;
    }

    public void setId(Long id) {
        Id = id;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }
}
