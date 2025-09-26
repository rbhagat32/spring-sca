export const SkeletonLoader = () => {
  return [...Array(12)].map((_, index) => (
    <div
      key={index}
      className="mx-auto h-9 w-[350px] animate-pulse rounded-2xl bg-zinc-800 px-3 py-2 shadow-lg"
    />
  ));
};
