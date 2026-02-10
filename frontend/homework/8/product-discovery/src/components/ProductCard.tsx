import { useMemo, useState } from "react";
import type { Product } from "../types/product";
import { formatCurrency } from "../lib/format";
import { FiStar } from "react-icons/fi";

type ProductCardProps = {
  readonly product: Product;
  readonly onClick: () => void;
};

export default function ProductCard({ product, onClick }: ProductCardProps) {
  const [hovered, setHovered] = useState(false);

  const hasDiscount = useMemo(
    () => product.discountPercentage > 0,
    [product.discountPercentage]
  );

  const discountedPrice = useMemo(() => {
    if (!hasDiscount) return product.price;
    return product.price * (1 - product.discountPercentage / 100);
  }, [hasDiscount, product.price, product.discountPercentage]);

  return (
    <button
      type="button"
      className="product-card"
      onClick={onClick}
      onMouseEnter={() => setHovered(true)}
      onMouseLeave={() => setHovered(false)}
      aria-pressed={hovered}
    >
      <img className="product-card__img" src={product.thumbnail} alt={product.title} />

      <div className="product-card__title">{product.title}</div>

      <div className="product-card__price">
        {hasDiscount ? (
          <>
            {formatCurrency(discountedPrice)}

            <span className="product-card__price-original">
              {formatCurrency(product.price)}
            </span>

            <span className="product-card__badge">
              {product.discountPercentage}% off
            </span>
          </>
        ) : (
          <>{formatCurrency(product.price)}</>
        )}
      </div>


      <div className="product-card__meta"><span><FiStar aria-hidden="true" /> {product.rating} / 5</span></div>
      <div className="product-card__meta">
        <b>Brand:</b> {product.brand}
      </div>
    </button>
  );
}
