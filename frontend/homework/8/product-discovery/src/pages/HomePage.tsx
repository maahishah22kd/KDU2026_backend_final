import { useCallback, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import ProductCard from "../components/ProductCard";
import {useProductContext } from "../context/ProductContext";

export default function HomePage() {
  const navigate = useNavigate();
  const {products, loading, searchLoading, error, searchQuery, fetchAllProducts } =
    useProductContext();

  const handleCardClick = useCallback(
    (productId: number) => {
      navigate(`/product/${productId}`);
    },
    [navigate]
  );

  useEffect(() => {
     if(products.length===0 && searchQuery.trim()==="")
      fetchAllProducts();
    } ,[fetchAllProducts, products.length, searchQuery]);

  if (loading && products.length===0) return <p>Loading products...</p>;
  if (error) return <p style={{ color: "red" }}>Error: {error}</p>;
  if (!loading && !searchLoading && products.length === 0 && searchQuery.trim() !== "")
    return <p>No results found.</p>;


  return (
    <div className="home">

            <h1 className="home__title">Products</h1>
          {searchLoading && <p className="home__status">Searching...</p>}
        {!loading && !searchLoading && products.length === 0 && searchQuery.trim() !== "" && (
          <p className="home__status">No results found.</p>
        )}

          <div className="home__grid">
            {products.map((p) => (
              <ProductCard
                key={p.id}
                product={p}
                onClick={() => handleCardClick(p.id)}
              />
            ))}
          </div>
    </div>
  );
}
