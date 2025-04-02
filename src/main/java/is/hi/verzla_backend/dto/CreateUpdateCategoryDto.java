package is.hi.verzla_backend.dto;

import jakarta.validation.constraints.NotEmpty;

public class CreateUpdateCategoryDto {
  @NotEmpty(message = "Category name cannot be empty")
  private String name;

  // Getters/Setters...
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }
}