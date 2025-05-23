import { fileURLToPath, URL } from 'node:url'

import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'
import vueJsx from '@vitejs/plugin-vue-jsx'
import AutoImport from 'unplugin-auto-import/vite'
import Components from 'unplugin-vue-components/vite'
import {
  vueDsfrAutoimportPreset,
  vueDsfrComponentResolver
} from '@gouvminint/vue-dsfr/meta'

const isCypress = process.env.CYPRESS === 'true'

// https://vitejs.dev/config/
export default defineConfig({
  base: process.env.BASE_URL || '/',
  plugins: [
    vue(),
    vueJsx(),
    AutoImport({
      include: [
        /\.[tj]sx?$/,
        /\.vue$/, /\.vue\?vue/,
      ],
      imports: [
        'vue',
        'vue-router',
        ...(isCypress ? [] : ['vitest']),
        vueDsfrAutoimportPreset,       // Autoimport des composables de VueDsfr
      ],
      vueTemplate: true,
      dts: './src/auto-imports.d.ts',
      eslintrc: {
        enabled: true,
        filepath: './.eslintrc-auto-import.json',
        globalsPropValue: true,
      },
    }),
    // Autoimport des composants utilisés dans les templates
    Components({
      extensions: ['vue'],
      dirs: ['src/components'],        // Autoimport de vos composants qui sont dans le dossier `src/components`
      include: [/\.vue$/, /\.vue\?vue/],
      dts: './src/components.d.ts',
      resolvers: [
        vueDsfrComponentResolver,      // Autoimport des composants de VueDsfr dans les templates
      ],
    }),
  ],
  resolve: {
    alias: {
      '@': fileURLToPath(new URL('./src', import.meta.url)),
    },
  },
  /**
   * Configuration de Vite pour le mode développement
   * Le proxy permet de rediriger les appels API vers le backend
   */
  server: {
    proxy: {
      '/api': {
        target: 'http://localhost:8080',
        changeOrigin: true
      }
    }
  }
})