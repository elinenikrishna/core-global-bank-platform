import { defineConfig } from "vite";
import react from "@vitejs/plugin-react";

export default defineConfig({
  plugins: [react()],
  server: { port: 5173 },
  preview: { port: 4173 },
  build: {
    chunkSizeWarningLimit: 900,
    rollupOptions: {
      output: {
        manualChunks: {
          "react-core": ["react", "react-dom", "react-router-dom"],
          "visual-engine": ["three", "@react-three/fiber", "@react-three/drei"],
          "motion-charts": ["framer-motion", "recharts"]
        }
      }
    }
  }
});
