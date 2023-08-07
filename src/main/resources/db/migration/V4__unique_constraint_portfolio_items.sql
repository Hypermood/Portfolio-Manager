ALTER TABLE portfolio_items
ADD CONSTRAINT unique_company_name_user_id UNIQUE (company_name,user_id);