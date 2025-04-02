package is.hi.verzla_backend.dto;

import java.util.UUID;

// DTO for returning User information via API
public class UserDto {
  private UUID id;
  private String name;
  private String email;

  // Default constructor
  public UserDto() {
  }

  // Constructor from User entity
  public UserDto(UUID id, String name, String email) {
    this.id = id;
    this.name = name;
    this.email = email;
  }

  // Getters and Setters
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

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }
}