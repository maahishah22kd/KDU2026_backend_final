import { NavLink, useNavigate } from "react-router-dom";
import { useEffect, useState } from "react";
import { useDispatch, useSelector } from "react-redux";
import type { RootState, AppDispatch } from "../store/store.ts"; 
import {
  fetchAllProductsThunk,
  searchProductsThunk,
} from "../features/products/productsThunks.ts";

import {
  setSearchQuery,
  clearSearch,
} from "../features/products/productsSlice";

export default function Navbar() {
  const dispatch = useDispatch<AppDispatch>();
  const searchQuery = useSelector((s: RootState) => s.products.searchQuery);
  const navigate= useNavigate();

  const cartCount = useSelector((s: RootState) =>
  s.cart.items.reduce((sum, item) => sum + item.quantity, 0)
  );
  const [localQuery, setLocalQuery] = useState(searchQuery);

  useEffect(() => {
    setLocalQuery(searchQuery);
  }, [searchQuery]);

  useEffect(() => {
    const timer = window.setTimeout(() => {
      dispatch(setSearchQuery(localQuery));

      if (localQuery.trim() === "") {
        dispatch(clearSearch());
        dispatch(fetchAllProductsThunk());
      } else {
        dispatch(searchProductsThunk(localQuery));
      }
    }, 500);

    return () => window.clearTimeout(timer);
  }, [localQuery, dispatch]);

  return (
    <header className="header">
      <div className="header__inner">
        <div className="header__brand">Product Discovery</div>

        <div className="header__search">
          <input
            className="header__searchInput"
            value={localQuery}
            onChange={(e) => setLocalQuery(e.target.value)}
            placeholder="Search for products..."
          />
          {localQuery.trim() !== "" && (
            <button
              type="button"
              className="header__clear"
              onClick={() => {
                setLocalQuery("");
                dispatch(clearSearch());
                dispatch(fetchAllProductsThunk());
              }}
              aria-label="Clear search"
            >
              Clear
            </button>
          )}
        </div>
<nav className="header__nav">
  <NavLink
    to="/"
    end
    className={({ isActive }) =>
      isActive ? "header__link header__link--active" : "header__link"
    }
  >
    Home
  </NavLink>

  <NavLink
    to="/cart"
    className={({ isActive }) =>
      isActive ? "header__link header__link--active" : "header__link"
    }
  >
    Cart {cartCount > 0 && (
      <span className="header__cartCount">{cartCount}</span>
    )}
  </NavLink>
</nav>

      </div>
    </header>
  );
}
