import TopBoards from "../components/topboards";

export default function Home() {
  return (
    <div
      className="
    flex flex-col
    items-center
    "
    >
      <div
        className="
      bg-gray-100
        mt-12 p-8
        max-w-[80vw]
        flex flex-col
        rounded-md
      "
      >
        <h2 className="text-4xl font-extrabold mb-4">SpringBoot Framework</h2>
        <p className="w-[55%] text-[1.4rem] mb-4">
          The Spring Framework provides a comprehensive programming and
          configuration model for modern Java-based enterprise applications - on
          any kind of deployment platform.
        </p>
        <TopBoards />
      </div>
    </div>
  );
}
