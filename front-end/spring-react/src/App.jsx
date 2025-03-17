import { useState } from "react";
import "./App.css";
import { BrowserRouter, NavLink, Route, Routes } from "react-router-dom";
import Home from "/src/pages/home";
import Board from "/src/pages/board";
import Photo from "/src/pages/photo";
import Navbar from "./components/navbar";

function App() {
  return (
    <BrowserRouter>
      <div className="w-[100vw] h-[100vh] flex flex-col">
        <Navbar />
        <Routes>
          <Route path="/" element={<Home />} />
          <Route path="/board" element={<Board />} />
          <Route path="/photo" element={<Photo />} />
        </Routes>
      </div>
    </BrowserRouter>
  );
}

export default App;
