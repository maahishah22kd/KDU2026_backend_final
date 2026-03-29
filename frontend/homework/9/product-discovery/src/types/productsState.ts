import type { Product } from "./product";

export interface ProductsState {
  products: Product[];
  searchQuery: string;
  selectedProduct: Product | null;

  loading: boolean;
  searchLoading: boolean;
  error: string | null;
}
