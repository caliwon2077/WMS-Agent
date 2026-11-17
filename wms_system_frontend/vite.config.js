import { defineConfig } from "vite";
import vue from "@vitejs/plugin-vue";

export default defineConfig({
  plugins: [vue()],
  server: {
    host: "127.0.0.1",
    port: 5173,
    strictPort: true,
    proxy: {
      "/java-api": {
        target: `http://127.0.0.1:${process.env.WMS_BACKEND_PORT || 8080}`,
        changeOrigin: true,
        rewrite: (path) => path.replace(/^\/java-api/, "/api")
      }
    }
  }
});
