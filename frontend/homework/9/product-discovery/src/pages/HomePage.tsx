import { useCallback, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import { useDispatch, useSelector } from "react-redux";

import ProductCard from "../components/ProductCard";
import { fetchAllProductsThunk } from "../features/products/productsThunks";
import type { RootState, AppDispatch } from "../store/store"; 

export default function HomePage() {
  const navigate = useNavigate();

  const dispatch = useDispatch<AppDispatch>();
  const { products, loading, searchLoading, error, searchQuery } = useSelector(
    (s: RootState) => s.products
  );

  const handleCardClick = useCallback(
    (productId: number) => {
      navigate(`/product/${productId}`);
    },
    [navigate]
  );

  useEffect(() => {
    if (products.length === 0 && searchQuery.trim() === "") {
      dispatch(fetchAllProductsThunk());
    }
  }, [dispatch, products.length, searchQuery]);

  if (loading && products.length === 0) return <p>Loading products...</p>;
  if (error)
    return (
      <p className="home__error" role="alert">
        Error: {error}
      </p>
    );

  return (
    <div className="home">
      <h1 className="home__title">Products</h1>

      {searchLoading && (
        <p className="home__status" role="status" aria-live="polite">
          Searching...
        </p>
      )}

      {!loading && !searchLoading && products.length === 0 && searchQuery.trim() !== "" && (
        <p className="home__status">No results found.</p>
      )}

      <div className="home__grid">
        {products.map((p) => (
          <ProductCard key={p.id} product={p} onClick={() => handleCardClick(p.id)} />
        ))}
      </div>
    </div>
  );
}
