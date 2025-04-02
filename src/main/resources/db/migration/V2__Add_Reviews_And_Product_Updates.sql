-- Drop the old ManyToMany join table
DROP TABLE IF EXISTS public.product_categories;

-- Modify the Product table
ALTER TABLE public.product
DROP COLUMN IF EXISTS image_url;

ALTER TABLE public.product
-- NUMERIC(3,2) fixed precision is preferred
ADD COLUMN rating DOUBLE PRECISION;

ALTER TABLE public.product
ADD COLUMN brand VARCHAR(255);

ALTER TABLE public.product
ADD COLUMN category_id UUID;

-- Add foreign key from Product to Category
ALTER TABLE public.product ADD CONSTRAINT fk_product_category FOREIGN KEY (category_id) REFERENCES public.category (id);

-- Create the new product_tags table (for @ElementCollection)
CREATE TABLE
  public.product_tags (product_id uuid NOT NULL, tag VARCHAR(255));

ALTER TABLE public.product_tags ADD CONSTRAINT fk_product_tags_product FOREIGN KEY (product_id) REFERENCES public.product (id);

-- Create the new review table
CREATE TABLE
  public.review (
    review_id uuid NOT NULL,
    comment TEXT,
    date TIMESTAMP
    WITH
      TIME ZONE NOT NULL,
      rating INTEGER NOT NULL CHECK (
        rating >= 1
        AND rating <= 5
      ),
      reviewer_email VARCHAR(255) NOT NULL,
      reviewer_name VARCHAR(255) NOT NULL,
      product_id uuid NOT NULL,
      PRIMARY KEY (review_id)
  );

ALTER TABLE public.review ADD CONSTRAINT fk_review_product FOREIGN KEY (product_id) REFERENCES public.product (id);

-- Index for faster review lookup by product_id
CREATE INDEX IF NOT EXISTS idx_review_product_id ON public.review (product_id);