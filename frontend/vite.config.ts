import { defineConfig } from 'vite'
import react from '@vitejs/plugin-react-swc'

// https://vite.dev/config/
export default defineConfig(({command}) => ({
  plugins: [react()],
  logLevel: command === 'build' ? 'silent': "info",
  server: {
    proxy: {
      '/api': {
        target: 'http://localhost:8080', // Spring Boot server
        changeOrigin: true,
      },
    },
  },
}));
