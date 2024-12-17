import React from "react";
import { BrowserRouter as Router, Routes, Route } from "react-router-dom";
import Navbar from "./components/navbar/Navbar"; // Navbar bileşeni
import Home from "./pages/Home"; // Yeni oluşturduğun Home bileşeni
import About from "./pages/SQLInfo"; // Yeni oluşturduğun About bileşeni


const App = () => {
  return (
    <Router>
      <Navbar />
      <Routes>
        {/* Home sayfası */}
        <Route path="/" element={<Home />} />
        {/* About sayfası */}
        <Route path="/about" element={<About />} />
      </Routes>
    </Router>
  );
};

export default App;
