package is.hi.verzla_backend.dto;

import java.util.UUID;

public class CategoryDto {
  private UUID id;
  private String name;

  // Getters/Setters...
  public UUID getId() {
    return id;
  }

  public void setId(UUID id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }
}
