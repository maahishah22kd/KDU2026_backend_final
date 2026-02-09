import { NavLink } from "react-router-dom";

export default function Navbar() {
  return (
    <header className="header">
      <div className="header__inner">
        <div className="header__brand">🛍️Product Discovery</div>

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
