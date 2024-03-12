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
        'uni-app',
        // 'pinia',
        // {
        //   'vue-router': [
        //     // 命名导入
        //     'useRouter', // import { useRouter } from 'vue-router' 
        //     'useRoute'
        //   ]
        // }
      ]
    }),
  ],
})
