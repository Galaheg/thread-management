import React from "react";
import { NavLink } from "react-router-dom"; // Router link için
import styles from "./Navbar.module.css"; // Navbar için ayrı bir CSS dosyası

const Navbar = () => {
  return (
    <nav className={styles.navbar}>
      <div className={styles.navBrand}>Thread Manager</div>
      <ul className={styles.navLinks}>
        <li>
          <NavLink to="/" className={({ isActive }) => (isActive ? styles.active : "")}>
            Home
          </NavLink>
        </li>
        <li>
          <NavLink to="/about" className={({ isActive }) => (isActive ? styles.active : "")}>
            About
          </NavLink>
        </li>
      </ul>
    </nav>
  );
};

export default Navbar;
