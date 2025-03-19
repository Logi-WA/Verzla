package is.hi.verzla_backend.entities;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.validation.constraints.NotEmpty;

/**
 * Represents a product category that can be associated with multiple products.
 */
@Entity
public class Category {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  @Column(name = "id", updatable = false, nullable = false)
  private UUID id;

  @NotEmpty(message = "Category name cannot be empty")
  private String name;

  @ManyToMany(mappedBy = "categories")
  private Set<Product> products = new HashSet<>();

  /**
   * Default constructor for JPA.
   */
  public Category() {}

  /**
   * Constructor to initialize the category with a name.
   *
   * @param name the name of the category.
   */
  public Category(String name) {
    this.name = name;
  }

  /**
   * Gets the ID of the category.
   *
   * @return the ID of the category.
   */
  public UUID getId() {
    return id;
  }

  /**
   * Sets the ID of the category.
   *
   * @param id the ID to set.
   */
  public void setId(UUID id) {
    this.id = id;
  }

  /**
   * Gets the name of the category.
   *
   * @return the name of the category.
   */
  public String getName() {
    return name;
  }

  /**
   * Sets the name of the category.
   *
   * @param name the name to set.
   */
  public void setName(String name) {
    this.name = name;
  }

  /**
   * Gets the set of products associated with this category.
   *
   * @return the set of products.
   */
  public Set<Product> getProducts() {
    return products;
  }

  /**
   * Sets the products associated with this category.
   *
   * @param products the set of products to associate with this category.
   */
  public void setProducts(Set<Product> products) {
    this.products = products;
  }
}
