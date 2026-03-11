import { useEffect, useState, useMemo, useCallback } from "react";
import { useNavigate, useParams } from "react-router-dom";
import { getProductById } from "../api/products";
import type { Product } from "../types/product";

export default function ProductDetailsPage() {
  const { id } = useParams();
  const navigate = useNavigate();

  const [product, setProduct] = useState<Product | null>(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);
  const [selectedImage, setSelectedImage] = useState<string>("");

  const handleBack = useCallback(() => {
    navigate(-1);
  }, [navigate]);

  const handleThumbnailClick = useCallback((img: string) => {
    setSelectedImage(img);
  }, []);

  useEffect(() => {
    async function loadProduct() {
      if (!id) {
        setError("Missing product id");
        setLoading(false);
        return;
      }

      try {
        setLoading(true);
        setError(null);

        const data = await getProductById(id);
        setProduct(data);
      } catch (err) {
        const message = err instanceof Error ? err.message : "Something went wrong";
        setError(message);
      } finally {
        setLoading(false);
      }
    }

    loadProduct();
  }, [id]);

  useEffect(() => {
    if (!product) return;
    setSelectedImage(product.thumbnail || product.images?.[0] || "");
  }, [product]);

  const hasDiscount = useMemo(() => {
    return (product?.discountPercentage ?? 0) > 0;
  }, [product?.discountPercentage]);

  const discountedPrice = useMemo(() => {
    if (!product) return 0;
    if (product.discountPercentage <= 0) return product.price;
    return product.price * (1 - product.discountPercentage / 100);
  }, [product?.price, product?.discountPercentage]);

  if (loading) return <p>Loading product...</p>;
  if (error) return <p className="product-details__error">Error: {error}</p>;
  if (!product) return <p>No product found.</p>;

  return (
    <div className="product-details">
      <div className="product-details__wrapper">
        <button type="button" className="btn product-details__back" onClick={handleBack}>
          ← Back
        </button>

        <div className="product-details__card">
          <div className="product-details__grid">
            <div>
              <div className="product-details__image-box">
                {selectedImage && (
                  <img
                    src={selectedImage}
                    alt={product.title}
                    className="product-details__main-image"
                  />
                )}
              </div>

              {product.images.length > 1 && (
                <div className="product-details__thumbs">
                  {product.images.map((img) => (
                    <button
                      key={img}
                      type="button"
                      onClick={() => handleThumbnailClick(img)}
                      className={
                        img === selectedImage
                          ? "product-details__thumb-btn product-details__thumb-btn--active"
                          : "product-details__thumb-btn"
                      }
                    >
                      <img src={img} alt="thumbnail" className="product-details__thumb-img" />
                    </button>
                  ))}
                </div>
              )}
            </div>

            <div>
              <div className="product-details__brand">{product.brand}</div>
              <h1 className="product-details__title">{product.title}</h1>

              <div className="product-details__meta">
                <span>📦 {product.stock} in stock</span>
                <span>🏷️ {product.category}</span>
                <span>⭐ {product.rating} / 5</span>
              </div>

              <div className="product-details__price">
                {hasDiscount ? (
                  <div className="product-details__price-row">
                    <span className="product-details__price-final">
                      ₹ {discountedPrice.toFixed(2)}
                    </span>
                    <span className="product-details__price-original">₹ {product.price}</span>
                    <span className="product-details__price-badge">
                      {product.discountPercentage}% off
                    </span>
                  </div>
                ) : (
                  <div className="product-details__price-final">₹ {product.price}</div>
                )}
              </div>

              <div className="product-details__section-title">Description</div>
              <p className="product-details__desc">{product.description}</p>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
}
