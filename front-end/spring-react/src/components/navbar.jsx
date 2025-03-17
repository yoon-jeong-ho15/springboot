import { Link } from "react-router-dom";

export default function Navbar() {
  return (
    <div
      className="
    flex flex-row
    bg-black text-white
    w-full py-3 px-2
    items-center
    "
    >
      <span className="text-2xl">SpringBoot project</span>
      <div
        className="
        w-40
        flex flex-row
        justify-between
        mx-8
      "
      >
        <Link to="/">Home</Link>
        <Link to="/board">Board</Link>
        <Link to="/photo">Photo</Link>
      </div>
      <div>서울 5℃ | 습도 30% | 강수확률 0%||미세먼지 : |초미세먼지 :</div>
    </div>
  );
}
