import { createSlice, type PayloadAction } from "@reduxjs/toolkit";
import type { ProductsState } from "../../types/productsState";
import {
  fetchAllProductsThunk,
  searchProductsThunk,
  fetchProductByIdThunk,
} from "./productsThunks";

const initialState: ProductsState = {
  products: [],
  searchQuery: "",
  selectedProduct: null,
  loading: false,
  searchLoading: false,
  error: null,
};

const productsSlice = createSlice({
  name: "products",
  initialState,

  reducers: {
    setSearchQuery: (state, action: PayloadAction<string>) => {
      state.searchQuery = action.payload;
    },

    clearSearch: (state) => {
      state.searchQuery = "";
      state.selectedProduct = null;
      state.error = null;
    },
  },

  extraReducers: (builder) => {
    builder
      .addCase(fetchAllProductsThunk.pending, (state) => {
        state.loading = true;
        state.error = null;
      })
      .addCase(fetchAllProductsThunk.fulfilled, (state, action) => {
        state.loading = false;
        state.products = action.payload;
      })
      .addCase(fetchAllProductsThunk.rejected, (state, action) => {
        state.loading = false;
        state.error = action.error.message ?? "Failed to fetch products";
      });

    builder
      .addCase(searchProductsThunk.pending, (state) => {
        state.searchLoading = true;
        state.error = null;
      })
      .addCase(searchProductsThunk.fulfilled, (state, action) => {
        state.searchLoading = false;
        state.products = action.payload;
      })
      .addCase(searchProductsThunk.rejected, (state, action) => {
        state.searchLoading = false;
        state.products = [];
        state.error = action.error.message ?? "Search failed";
      });

    builder
      .addCase(fetchProductByIdThunk.pending, (state) => {
        state.loading = true;
        state.selectedProduct = null;
        state.error = null;
      })
      .addCase(fetchProductByIdThunk.fulfilled, (state, action) => {
        state.loading = false;
        state.selectedProduct = action.payload;
      })
      .addCase(fetchProductByIdThunk.rejected, (state, action) => {
        state.loading = false;
        state.selectedProduct = null;
        state.error = action.error.message ?? "Failed to fetch product";
      });
  },
});

export const { setSearchQuery, clearSearch } = productsSlice.actions;
export default productsSlice.reducer;
