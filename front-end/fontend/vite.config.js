import { defineConfig } from 'vite'
import { resolve } from 'path'

import uni from '@dcloudio/vite-plugin-uni'
import AutoImport from 'unplugin-auto-import/vite'
import UnoCSS from 'unocss/vite'

// https://vitejs.dev/config/
export default defineConfig({
  resolve: {
    alias: {
      '@/': `${resolve(__dirname, 'src')}/`,
    },
  },
  plugins: [
    uni(),
    UnoCSS(),
    AutoImport({
      imports: [
        'vue',
        'uni-app'
      ]
    }),
  ],
  build: {
		watch: {
			exclude: ['node_modules/**', "/__uno.css"]
		},
	}
})
