import styles from "@/components/custom/page-loader.module.css";

export function PageLoader({ fullScreen = false }: { fullScreen?: boolean }) {
  return (
    <div
      className={`z-50 ${
        fullScreen ? "fixed h-screen w-screen" : "h-full w-full"
      } grid place-items-center`}
    >
      <div className={styles.loader} />
    </div>
  );
}
