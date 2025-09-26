import styles from "@/components/partials/page-loader.module.css";

const PageLoader = ({ fullScreen = false }: { fullScreen?: boolean }) => {
  return (
    <div
      className={`z-50 ${
        fullScreen ? "fixed h-screen w-screen" : "h-full w-full"
      } grid place-items-center`}
    >
      <div className={styles.loader}></div>
    </div>
  );
};

export { PageLoader };
