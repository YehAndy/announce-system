import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'

export default defineConfig({
  plugins: [vue()],
  server: {
    proxy: {
      '/api': 'http://backend:8080'
    }
  },
  optimizeDeps: {
      include: ['@ckeditor/ckeditor5-build-classic']
  }
})
