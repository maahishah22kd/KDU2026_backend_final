import { createAsyncThunk } from "@reduxjs/toolkit";
import type { Product, ProductsListResponse } from "../../types/product";

const BASE_URL = import.meta.env.VITE_API_BASE_URL ?? "https://dummyjson.com";

export const fetchAllProductsThunk = createAsyncThunk(
  "products/fetchAll",
  async (): Promise<Product[]> => {
    const response = await fetch(`${BASE_URL}/products`);
    if (!response.ok) {
      throw new Error(`Failed to fetch products: ${response.status}`);
    }
    const data: ProductsListResponse = await response.json();
    return data.products;
  }
);

export const fetchProductByIdThunk = createAsyncThunk(
  "products/fetchById",
  async (id: string): Promise<Product> => {
    const response = await fetch(`${BASE_URL}/products/${id}`);
    if (!response.ok) {
      throw new Error(`Failed to fetch product with id:${id} ${response.status}`);
    }
    const data: Product = await response.json();
    return data;
  }
);

export const searchProductsThunk = createAsyncThunk(
  "products/search",
  async (searchQuery: string): Promise<Product[]> => {
    const trimmed = searchQuery.trim();
    if (!trimmed) return [];
    const response = await fetch(
      `${BASE_URL}/products/search?q=${encodeURIComponent(trimmed)}`
    );
    if (!response.ok) {
      throw new Error(`Failed to search products: ${response.status}`);
    }
    const data: ProductsListResponse = await response.json();
    return data.products;
  }
);
