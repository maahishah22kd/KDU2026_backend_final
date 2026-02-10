import { createContext, useCallback, useContext, useMemo, useState } from "react";
import type { ReactNode } from "react";
import type { Product, ProductsListResponse } from "../types/product";
import { getAllProducts, getProductById, searchProducts } from "../api/products";

type ProductContextValue = {
  products: Product[];
  selectedProduct: Product | null;
  searchQuery: string;

  loading: boolean;
  searchLoading: boolean;
  error: string | null;

  fetchAllProducts: () => Promise<void>;
  fetchProductById: (id: string | number) => Promise<void>;
  searchForProducts: (query: string) => Promise<void>;

  setSearchQuery: (query: string) => void;
  clearSearch: () => Promise<void>;
};

const ProductContext = createContext<ProductContextValue | undefined>(undefined);

export function ProductProvider({ children }: { children: ReactNode }) {
  const [products, setProducts] = useState<Product[]>([]);
  const [selectedProduct, setSelectedProduct] = useState<Product | null>(null);
  const [searchQuery, setSearchQueryState] = useState("");

  const [loading, setLoading] = useState(false);
  const [searchLoading, setSearchLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);

  const fetchAllProducts = useCallback(async() => {
    try {
      setLoading(true);
      setError(null);

      const data: ProductsListResponse = await getAllProducts();
      setProducts(data.products);
    } catch (err) {
      const message = err instanceof Error ? err.message : "Something went wrong";
      setError(message);
    } finally {
      setLoading(false);
    }
  }, []);

  const fetchProductByIdAction = useCallback(async(id: string | number) => {
    try {
      setLoading(true);
      setError(null);

      const data = await getProductById(id);
      setSelectedProduct(data);
    } catch (err) {
      const message = err instanceof Error ? err.message : "Something went wrong";
      setError(message);
      setSelectedProduct(null);
    } finally {
      setLoading(false);
    }
  }, []);

  const searchForProducts = useCallback(async(query: string) => {
    const trimmed = query.trim();

    if (!trimmed) {
      await fetchAllProducts();
      return;
    }

    try {
      setSearchLoading(true);
      setError(null);

      const data = await searchProducts(trimmed);
      const q = trimmed.toLowerCase();

        const filtered = data.products.filter((p) => {
        const title = (p.title ?? "").toLowerCase();
        const brand = (p.brand ?? "").toLowerCase();
        const category = (p.category ?? "").toLowerCase();

        return title.includes(q) || brand.includes(q) || category.includes(q);
        });

        setProducts(filtered);
    } catch (err) {
      const message = err instanceof Error ? err.message : "Something went wrong";
      setError(message);
      setProducts([]);
    } finally {
      setSearchLoading(false);
    }
  }, [fetchAllProducts]);

  const setSearchQuery = useCallback((query: string) => {
    setSearchQueryState(query);
  }, []);

  const clearSearch = useCallback(async () => {
    setSearchQueryState("");
    setError(null);
    setSelectedProduct(null);
    await fetchAllProducts();
  }, [fetchAllProducts]);

  const value = useMemo<ProductContextValue>(
    () => ({
      products,
      selectedProduct,
      searchQuery,
      loading,
      searchLoading,
      error,
      fetchAllProducts,
      fetchProductById: fetchProductByIdAction,
      searchForProducts,
      setSearchQuery,
      clearSearch,
    }),
    [
      products,
      selectedProduct,
      searchQuery,
      loading,
      searchLoading,
      error,
      fetchAllProducts,
      fetchProductByIdAction,
      searchForProducts,
      setSearchQuery,
      clearSearch,
    ]
  );

  return <ProductContext.Provider value={value}>{children}</ProductContext.Provider>;
}

export function useProductContext(): ProductContextValue {
  const ctx = useContext(ProductContext);
  if (!ctx) throw new Error("useProductContext must be used within ProductProvider");
  return ctx;
}
