import {
    defineConfig,
    presetAttributify,
    presetIcons,
    transformerDirectives,
    transformerVariantGroup,
  } from 'unocss'
  
  import {
    presetApplet,
    presetRemToRpx,
    transformerApplet,
    transformerAttributify,
  } from 'unocss-applet'
  
  
  const isApplet = process.env?.UNI_PLATFORM?.startsWith('mp')
  
  export default defineConfig({
    shortcuts: {
      'bg-base': 'bg-gray-100 dark:bg-dark',
      'bg-base-second': 'bg-white dark:bg-dark-100',
      'color-base': 'text-gray-700 dark:text-light-2',
      'color-base-second': 'text-gray-400 dark:text-gray-500/60',
      'border-base': 'border border-gray-200 dark:border-gray/60',
      'bg-primary': 'bg-light-blue-500 dark:bg-light-blue-600',
    },
    presets: [
      presetIcons({
        scale: 1.2,
        warn: true,
        extraProperties: {
          'display': 'inline-block',
          'vertical-align': 'middle',
        },
      }),
      presetApplet({ enable: isApplet }),
      /**
       * you can add `presetAttributify()` here to enable unocss attributify mode prompt
       * although preset is not working for applet, but will generate useless css
       */
      presetAttributify(),
      presetRemToRpx({ enable: isApplet }),
    ],
    transformers: [
      transformerDirectives(),
      transformerVariantGroup(),
      // Don't change the following order
      transformerAttributify({ enable: isApplet }),
      transformerApplet({ enable: isApplet }),
    ],
    rules: [
      [
        'p-safe',
        {
          padding:
            'env(safe-area-inset-top) env(safe-area-inset-right) env(safe-area-inset-bottom) env(safe-area-inset-left)',
        },
      ],
      ['pt-safe', { 'padding-top': 'env(safe-area-inset-top)' }],
      ['pb-safe', { 'padding-bottom': 'env(safe-area-inset-bottom)' }],
    ],
  })
  