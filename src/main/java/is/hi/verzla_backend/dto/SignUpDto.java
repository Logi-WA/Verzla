package is.hi.verzla_backend.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

public class SignUpDto {

  @NotEmpty(message = "Name cannot be empty")
  private String name;

  @Email(message = "Email should be valid")
  @NotEmpty(message = "Email cannot be empty")
  private String email;

  @NotEmpty(message = "Password cannot be empty")
  @Size(min = 4, message = "Password must be at least 4 characters long")
  private String password;

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

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }
}