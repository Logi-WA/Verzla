package is.hi.verzla_backend.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;

public class UpdateUserDto {

  @NotEmpty(message = "Name cannot be empty")
  private String name;

  @Email(message = "Email should be valid")
  @NotEmpty(message = "Email cannot be empty")
  private String email;

  // Getters and Setters
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }
}