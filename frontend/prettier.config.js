import { fileURLToPath } from 'node:url';

/** @type {import("prettier").Config} */
const config = {
  // === Prettier core formatting ===
  tabWidth: 2,
  semi: true,
  singleQuote: true,
  trailingComma: 'all',
  bracketSpacing: true,
  arrowParens: 'always',

  // === Plugins ===
  plugins: [
    'prettier-plugin-tailwindcss',
    '@ianvs/prettier-plugin-sort-imports',
  ],

  // Tailwind plugin settings
  tailwindFunctions: ['cn', 'cva'],
  tailwindStylesheet: fileURLToPath(
    new URL('./src/index.css', import.meta.url),
  ),

  // Import sorting settings
  importOrder: [
    '<TYPES>', // Type imports first
    '^(react/(.*)$)|^(react$)', // React imports
    '<THIRD_PARTY_MODULES>', // Other npm packages
    '^@/components/(.*)$', // Aliased components
    '^@/hooks/(.*)$', // Aliased hooks
    '^@/lib/(.*)$', // Aliased utils
    '^[../]', // Relative imports from parent dirs
    '^[./]', // Relative imports from current dir
  ],
  importOrderParserPlugins: ['typescript', 'jsx', 'decorators-legacy'],
  importOrderTypeScriptVersion: '5.9.2',

  // File-specific overrides
  overrides: [
    { files: '*.json', options: { parser: 'json' } },
    { files: '*.md', options: { parser: 'markdown' } },
    { files: '*.css', options: { parser: 'css' } },
  ],
};

export default config;
