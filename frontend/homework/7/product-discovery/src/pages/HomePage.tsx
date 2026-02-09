import { useCallback, useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import { getAllProducts } from "../api/products";
import type { Product } from "../types/product";
import ProductCard from "../components/ProductCard";

export default function HomePage() {
  const [products, setProducts] = useState<Product[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  const navigate = useNavigate();

  const handleCardClick = useCallback(
    (productId: number) => {
      navigate(`/product/${productId}`);
    },
    [navigate]
  );

  useEffect(() => {
    async function loadProducts() {
      try {
        setLoading(true);
        setError(null);

        const data = await getAllProducts();
        setProducts(data.products);
      } catch (err) {
        const message = err instanceof Error ? err.message : "Something went wrong";
        setError(message);
      } finally {
        setLoading(false);
      }
    }

    loadProducts();
  }, []);

  if (loading) return <p>Loading products...</p>;
  if (error) return <p style={{ color: "red" }}>Error: {error}</p>;

  return (
    <div className="home">
      <h1 className="home__title">Products</h1>

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
