# Verzla Backend

This repository houses the codebase for a persistent RESTful API server for the Verzla Android
application. The backend is built with Spring Boot and provides authentication, product management,
user management, cart and wishlist functionality, and more.

## Project Overview

Verzla is an e-commerce platform designed for mobile devices, with this backend providing all
necessary services for the Android application. The API follows RESTful principles and uses
JWT-based authentication for secure access.

## Technology Stack

- **Framework**: Spring Boot
- **Database**: PostgreSQL (configured via Spring Data JPA)
- **Authentication**: JWT (JSON Web Tokens)
- **Documentation**: OpenAPI 3.0 (Swagger)
- **Database Migration**: Flyway

## API Documentation

API documentation is available through Swagger UI when the application is running:

- Local: `http://localhost:8080/swagger-ui.html`
- Production: `https://verzla-71cda7a37a2e.herokuapp.com/swagger-ui.html`

## Architecture

The application follows a layered architecture:

- **Controllers**: Handle HTTP requests and responses
- **Services**: Contain business logic
- **Repositories**: Provide data access
- **Entities**: Map to database tables
- **DTOs**: Transfer data between layers
- **Security**: Handle authentication and authorization

## API Endpoints

The following sections list the primary API endpoints grouped by functionality.

### Authentication

- `POST /auth/login` - Authenticate user and receive JWT token

### Categories

- `GET /api/categories` - Retrieve all product categories
- `GET /api/categories/{categoryId}` - Retrieve a specific category by ID
- `GET /api/categories/name/{name}` - Retrieve a specific category by name
- `POST /api/categories` - Create a new category
- `PUT /api/categories/{categoryId}` - Update an existing category
- `DELETE /api/categories/{categoryId}` - Delete a category
- `GET /api/categories/{categoryId}/product-count` - Get the number of products in a category
- `GET /api/products/{productId}/categories` - Get all categories for a specific product

### Products

- `GET /api/products` - Retrieve all products
- `GET /api/products/{id}` - Retrieve a single product by ID
- `POST /api/products` - Create a new product
- `PATCH /api/products/{id}/name` - Update a product's name
- `PATCH /api/products/{id}/description` - Update a product's description

### Users

- `GET /api/users` - Retrieve all users
- `POST /api/users` - Create a new user
- `GET /api/users/me` - Retrieve current user's data
- `PATCH /api/users/me` - Update current user's profile
- `PATCH /api/users/me/password` - Update current user's password
- `DELETE /api/users/{userId}` - Delete a user

### Cart

- `GET /api/cart` - Retrieve user's cart
- `POST /api/cart` - Add item to cart
- `PATCH /api/cart/{id}` - Update cart item quantity
- `DELETE /api/cart/{cartItemId}` - Remove item from cart
- `POST /api/cart/buy` - Process purchase of cart items

### Wishlist

- `GET /api/wishlist` - Retrieve user's wishlist
- `POST /api/wishlist` - Add item to wishlist
- `DELETE /api/wishlist/{wishlistItemId}` - Remove item from wishlist
- `POST /api/wishlist/addAllToCart` - Add all wishlist items to cart
- `DELETE /api/wishlist/clear` - Clear user's wishlist
