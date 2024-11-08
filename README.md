# Endpoints

## Assignment 2

1. `POST /auth/login` *(1)*
2. `GET /api/categories` *(1)*

## Assignment 3

### Ass. 3 - Endpoints

1. `GET /api/products` *(1)*  
    Retrieve all resources in a collection (e.g. All users, all items)
2. `GET /api/categories` *(1)*  
    Retrieve all resources in a collection (e.g. All users, all items)
3. `GET /api/users` *(1)*  
    Retrieve all resources in a collection (e.g. All users, all items)
4. `GET /api/products/{id}` *(1)*  
    Retrieve a single resource by ID (e.g. Item, user, order based on unique identifier)
5. `POST /api/users` *(2)*  
    Create new resource (e.g. New item, new user account)
6. `GET /api/users/me` *(2)*  
    Retrieve user-specific data (e.g. User profile, user settings)
7. `PATCH /api/users/me` *(2)*  
    Partially update resource details (e.g. update user profile, change multiple item details)
8. `PATCH /api/users/me/password` *(2)*  
    Partially update resource details (e.g. update user profile, change multiple item details)
9. `POST /api/cart` *(2)*  
    Create an association between resources (e.g. Add item to users favourites)
10. `POST /api/wishlist` *(2)*  
    Create an association between resources (e.g. Add item to users favourites)
11. `GET /api/cart/cart` / `GET /api/cart` *(2)*  
    Retrieve associated resouces (e.g. User favourites, user orders)
12. `GET /api/cart/wishlist` / `GET /api/wishlist` *(2)*  
    Retrieve associated resouces (e.g. User favourites, user orders)
13. `PATCH /api/cart/{id}` *(3)*  
    Update nested resource (e.g. update specific attributes of items in an order)

## Assignment 4

New view `admin` added. Accessible in the dropdown menu in the top right for logged in users.

### Ass. 4 - Functional Endpoints

1. `GET /api/users` *(1)*
    The new admin dashboard provides a table of all users in the database.
2. `GET /api/products` *(1)*
    The admin dashboard also provides a table of all products in the database.
3. `POST /api/products` *(2)*
    A button has been added to the wishlist page to add a product to a cart.
4. `DELETE /api/wishlist/{wishlistItemId}` *(2)*
    A button has been added to the wishlist page to remove a product from a wishlist.
5. `DELETE /api/cart/{cartItemId}` *(2)*
    A button has been added to the cart page to remove a product from a cart.
6. `PATCH /api/cart/{cartItemId}` *(3)*
    Buttons have been added to the cart page to increment/decrement the quantity of an item in a cart
