import { useMemo } from "react";
import { useNavigate } from "react-router-dom";
import { useDispatch, useSelector } from "react-redux";
import type { RootState, AppDispatch } from "../store/store";
import { decreaseQty, increaseQty, removeFromCart } from "../features/cart/cartSlice";
import { formatCurrency } from "../lib/format";

export default function CartPage() {
  const navigate = useNavigate();
  const dispatch = useDispatch<AppDispatch>();

  const items = useSelector((s: RootState) => s.cart.items);

  const totalItems = useMemo(
    () => items.reduce((sum, i) => sum + i.quantity, 0),
    [items]
  );

  const totalPrice = useMemo(
    () => items.reduce((sum, i) => sum + i.product.price * i.quantity, 0),
    [items]
  );

  if (items.length === 0) {
    return (
      <div>
        <h1>Shopping Cart</h1>
        <p>Cart is empty</p>
        <button className="btn" onClick={() => navigate("/")}>
          Continue Shopping
        </button>
      </div>
    );
  }

  return (
    <div>
      <h1>Shopping Cart</h1>

      <div >
        <div>
          {items.map(({ product, quantity }) => {
            const itemTotal = product.price * quantity;

            return (
              <div key={product.id}>
                <img
                  src={product.thumbnail}
                  alt={product.title}
                />

                <div>
                  <div>{product.title}</div>
                  <div>{formatCurrency(product.price)}</div>
                </div>

                <div>
                  <button
                    type="button"
                    onClick={() => dispatch(decreaseQty(product.id))}
                    aria-label="Decrease quantity"
                  >
                    −
                  </button>

                  <span>{quantity}</span>

                  <button
                    type="button"
                    onClick={() => dispatch(increaseQty(product.id))}
                    aria-label="Increase quantity"
                  >
                    +
                  </button>
                </div>

                <div>{formatCurrency(itemTotal)}</div>

                <button
                  type="button"
                  
                  onClick={() => dispatch(removeFromCart(product.id))}
                >
                  Remove
                </button>
              </div>
            );
          })}
        </div>

        <aside>
          <h2>Order Summary</h2>

          <div>
            <span>Total Items:</span>
            <span>{totalItems}</span>
          </div>

          <div>
            <span>Total Price:</span>
            <span>{formatCurrency(totalPrice)}</span>
          </div>

          <button onClick={() => navigate("/")}>
            Continue Shopping
          </button>
        </aside>
      </div>
    </div>
  );
}
