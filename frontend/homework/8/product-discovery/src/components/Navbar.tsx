import { NavLink } from "react-router-dom";
import { useProductContext } from "../context/ProductContext";
import { useEffect, useState } from "react";
//outlet can be used here, if entire child page needs to be re-rendered, but navbar stays the same

export default function Navbar() {
 const {searchQuery, setSearchQuery, searchForProducts, clearSearch}=useProductContext();
 const [localQuery, setLocalQuery]= useState(searchQuery);

 useEffect(() => {
    setLocalQuery(searchQuery);
  }, [searchQuery]);

  useEffect(() => {
    const timer = window.setTimeout(() => {
      setSearchQuery(localQuery);

      if (localQuery.trim() === "") {
        clearSearch();
      } else {
        searchForProducts(localQuery);
      }
    }, 500);

    return () => window.clearTimeout(timer);
  }, [localQuery, setSearchQuery, searchForProducts, clearSearch]);

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
                clearSearch();
              }}
              aria-label="Clear search"
            > Clear
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
        </nav>
      </div>
    </header>
  );
}
