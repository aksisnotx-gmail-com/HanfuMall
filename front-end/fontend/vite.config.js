import { defineConfig } from 'vite'
import { resolve } from 'path'

import uni from '@dcloudio/vite-plugin-uni'
import AutoImport from 'unplugin-auto-import/vite'
import UnoCSS from 'unocss/vite'

// https://vitejs.dev/config/
export default defineConfig({
  plugins: [
    uni(),
    UnoCSS(),
    AutoImport({
      imports: [
        'vue',
        'pinia',
        'uni-app'
      ]
    }),
  ],
  resolve: {
    alias: {
      '@/': `${resolve(__dirname, 'src')}/`,
    },
  },
  // server: {
  //   proxy: {
  //     '/api': {
  //       target: 'http://localhost:8080',
  //       changeOrigin: true,
  //       rewrite: path => path.replace(/^\/api/, '')
  //     }
  //   }
  // },
  build: {
		watch: {
			exclude: ['node_modules/**', "/__uno.css"]
		},
	}
})
