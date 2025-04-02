-- Users Table
CREATE TABLE
  public.users (
    id uuid NOT NULL,
    email character varying(255) NOT NULL,
    name character varying(255),
    password character varying(255) NOT NULL
  );

ALTER TABLE ONLY public.users ADD CONSTRAINT users_pkey PRIMARY KEY (id);

ALTER TABLE ONLY public.users ADD CONSTRAINT users_email_key UNIQUE (email);

-- Category Table (Included as it existed in V1)
CREATE TABLE
  public.category (id uuid NOT NULL, name character varying(255));

ALTER TABLE ONLY public.category ADD CONSTRAINT category_pkey PRIMARY KEY (id);

-- Product Table (Included with old columns as it existed in V1)
CREATE TABLE
  public.product (
    id uuid NOT NULL,
    name character varying(255),
    description character varying(255),
    image_url character varying(255), -- Old column
    price double precision NOT NULL
  );

ALTER TABLE ONLY public.product ADD CONSTRAINT product_pkey PRIMARY KEY (id);

-- Product Categories Join Table (Included as it existed in V1)
CREATE TABLE
  public.product_categories (
    product_id uuid NOT NULL,
    category_id uuid NOT NULL
  );

ALTER TABLE ONLY public.product_categories ADD CONSTRAINT product_categories_pkey PRIMARY KEY (product_id, category_id);

-- Carts Table
CREATE TABLE
  public.carts (id uuid NOT NULL, user_id uuid NOT NULL);

ALTER TABLE ONLY public.carts ADD CONSTRAINT carts_pkey PRIMARY KEY (id);

-- Cart Items Table
CREATE TABLE
  public.cart_items (
    id uuid NOT NULL,
    quantity integer NOT NULL,
    cart_id uuid NOT NULL,
    product_id uuid NOT NULL
  );

ALTER TABLE ONLY public.cart_items ADD CONSTRAINT cart_items_pkey PRIMARY KEY (id);

-- Wishlists Table
CREATE TABLE
  public.wishlists (id uuid NOT NULL, user_id uuid NOT NULL);

ALTER TABLE ONLY public.wishlists ADD CONSTRAINT wishlists_pkey PRIMARY KEY (id);

-- Wishlist Items Table
CREATE TABLE
  public.wishlist_items (
    id uuid NOT NULL,
    product_id uuid NOT NULL,
    wishlist_id uuid NOT NULL
  );

ALTER TABLE ONLY public.wishlist_items ADD CONSTRAINT wishlist_items_pkey PRIMARY KEY (id);

-- Orders Table
CREATE TABLE
  public.orders (
    id uuid NOT NULL,
    order_date timestamp without time zone NOT NULL,
    status character varying(255) NOT NULL,
    user_id uuid NOT NULL
  );

ALTER TABLE ONLY public.orders ADD CONSTRAINT orders_pkey PRIMARY KEY (id);

-- Order Items Table
CREATE TABLE
  public.order_items (
    id uuid NOT NULL,
    price double precision NOT NULL,
    quantity integer NOT NULL,
    order_id uuid NOT NULL,
    product_id uuid NOT NULL
  );

ALTER TABLE ONLY public.order_items ADD CONSTRAINT order_items_pkey PRIMARY KEY (id);

-- Foreign Key Constraints from V1
ALTER TABLE ONLY public.product_categories ADD CONSTRAINT product_categories_category_id_fkey FOREIGN KEY (category_id) REFERENCES public.category (id);

ALTER TABLE ONLY public.product_categories ADD CONSTRAINT product_categories_product_id_fkey FOREIGN KEY (product_id) REFERENCES public.product (id);

ALTER TABLE ONLY public.carts ADD CONSTRAINT carts_user_id_fkey FOREIGN KEY (user_id) REFERENCES public.users (id);

ALTER TABLE ONLY public.cart_items ADD CONSTRAINT cart_items_cart_id_fkey FOREIGN KEY (cart_id) REFERENCES public.carts (id);

ALTER TABLE ONLY public.cart_items ADD CONSTRAINT cart_items_product_id_fkey FOREIGN KEY (product_id) REFERENCES public.product (id);

ALTER TABLE ONLY public.wishlists ADD CONSTRAINT wishlists_user_id_fkey FOREIGN KEY (user_id) REFERENCES public.users (id);

ALTER TABLE ONLY public.wishlist_items ADD CONSTRAINT wishlist_items_product_id_fkey FOREIGN KEY (product_id) REFERENCES public.product (id);

ALTER TABLE ONLY public.wishlist_items ADD CONSTRAINT wishlist_items_wishlist_id_fkey FOREIGN KEY (wishlist_id) REFERENCES public.wishlists (id);

ALTER TABLE ONLY public.orders ADD CONSTRAINT orders_user_id_fkey FOREIGN KEY (user_id) REFERENCES public.users (id);

ALTER TABLE ONLY public.order_items ADD CONSTRAINT order_items_order_id_fkey FOREIGN KEY (order_id) REFERENCES public.orders (id);

ALTER TABLE ONLY public.order_items ADD CONSTRAINT order_items_product_id_fkey FOREIGN KEY (product_id) REFERENCES public.product (id);