import { useCallback, useEffect, useMemo, useState } from "react";
import { useNavigate, useParams } from "react-router-dom";
import { fetchProductByIdThunk } from "../features/products/productsThunks.ts";
import { formatCurrency } from "../lib/format";
import { FiBox, FiTag, FiStar } from "react-icons/fi";
import type { RootState, AppDispatch } from "../store/store.ts"; 
import { useDispatch, useSelector } from "react-redux";
import { addToCart } from "../features/cart/cartSlice.ts";


export default function ProductDetailsPage() {
  const { id } = useParams();
  const navigate = useNavigate();

  const dispatch = useDispatch<AppDispatch>();
 const { selectedProduct: product, loading, error } = useSelector(
    (s: RootState) => s.products
  );
  const [selectedImage, setSelectedImage] = useState<string>("");

  const handleBack = useCallback(() => navigate(-1), [navigate]);
  const handleThumbnailClick = useCallback((img: string) => setSelectedImage(img), []);

  useEffect(() => {
    if (!id) return;
    dispatch(fetchProductByIdThunk(id));
  }, [id, dispatch]);

  useEffect(() => {
    if (!product) return;
    const first = product.thumbnail || (product.images.length > 0 ? product.images[0] : "");
    setSelectedImage(first);
  }, [product]);

  const hasDiscount = useMemo(
    () => (product?.discountPercentage ?? 0) > 0,
    [product?.discountPercentage]
  );

  const discountedPrice = useMemo(() => {
    if (!product) return 0;
    if (product.discountPercentage <= 0) return product.price;
    return product.price * (1 - product.discountPercentage / 100);
  }, [product?.price, product?.discountPercentage]);

  const handleAddToCart=()=>{
    if(!product) return;
    dispatch(addToCart(product));
  }

  if (!id) return <p className="product-details__error" role="alert">Missing product id</p>;
  if (loading) return <p role="status" aria-live="polite">Loading product...</p>;
  if (error) return <p className="product-details__error" role="alert">Error: {error}</p>;
  if (!product) return null;

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
                  {product.images.map((img, index) => (
                    <button
                      key={`thumb-${index}-${img}`}
                      type="button"
                      onClick={() => handleThumbnailClick(img)}
                      className={
                        img === selectedImage
                          ? "product-details__thumb-btn product-details__thumb-btn--active"
                          : "product-details__thumb-btn"
                      }
                      aria-label={`View image ${index + 1}`}
                    >
                      <img src={img} alt="" className="product-details__thumb-img" />
                    </button>
                  ))}
                </div>
              )}
            </div>

            <div>
              <div className="product-details__brand">{product.brand}</div>
              <h1 className="product-details__title">{product.title}</h1>

              <div className="product-details__meta">
                <span><FiBox aria-hidden="true" /> {product.stock} in stock</span>
                <span><FiTag aria-hidden="true" /> {product.category}</span>
                <span><FiStar aria-hidden="true" /> {product.rating} / 5</span>
              </div>

              <div className="product-details__price">
                {hasDiscount ? (
                  <div className="product-details__price-row">
                    <span className="product-details__price-final">
                      {formatCurrency(discountedPrice)}
                    </span>
                    <span className="product-details__price-original">
                      {formatCurrency(product.price)}
                    </span>
                    <span className="product-details__price-badge">
                      {product.discountPercentage}% off
                    </span>
                  </div>
                ) : (
                  <div className="product-details__price-final">
                    {formatCurrency(product.price)}
                  </div>
                )}
              </div>

              <div className="product-details__section-title">Description</div>
              <p className="product-details__desc">{product.description}</p>
              <button
              type="button"
              className="btn"
              onClick={
                ()=>{
                  if (!product) return;
                  dispatch(addToCart(product));
                  navigate("/cart")
                }
              }
              >
          Add to cart
        </button>
            </div>

          </div>
        </div>
      </div>
    </div>
  );
}
