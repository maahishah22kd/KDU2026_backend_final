const BASE_URL = import.meta.env.VITE_API_BASE_URL ?? "https://dummyjson.com";
import type { Product, ProductsListResponse } from "../types/product";

async function request<T>(path:string): Promise<T>{
    const res = await fetch(`${BASE_URL}${path}`);

  if (!res.ok) {
    const text = await res.text().catch(() => "");
    throw new Error(`API error ${res.status}: ${text || res.statusText}`);
  }

  return res.json() as Promise<T>;
}

export function getAllProducts(): Promise<ProductsListResponse> {
  return request<ProductsListResponse>("/products");
}

export function getProductById(id: string | number): Promise<Product> {
  return request<Product>(`/products/${id}`);
}

export function searchProducts(query: string): Promise<ProductsListResponse>{
  const q= encodeURIComponent(query.trim());
  return request<ProductsListResponse>(`/products/search?q=${q}`);
}