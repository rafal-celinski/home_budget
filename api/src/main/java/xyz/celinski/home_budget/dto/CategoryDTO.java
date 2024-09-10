package xyz.celinski.home_budget.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import xyz.celinski.home_budget.dto.validation.groups.OnDisplay;
import xyz.celinski.home_budget.dto.validation.groups.OnCreate;
import xyz.celinski.home_budget.dto.validation.groups.OnUpdate;

public class CategoryDTO {
    @Null(groups = {OnCreate.class, OnUpdate.class})
    @NotNull(groups = {OnDisplay.class})
    private Long id;
    @NotBlank
    private String name;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
