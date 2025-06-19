// @ts-check
import { defineConfig } from 'astro/config';
import starlight from '@astrojs/starlight';
import tailwindcss from '@tailwindcss/vite'

import react from '@astrojs/react';

// https://astro.build/config
export default defineConfig({
	vite: {
		plugins: [tailwindcss()],
	},
	integrations: [starlight({
		title: 'NextCP/2 Documentation',
		head: [
			{
				tag: 'script',
				attrs: {
					src: 'https://cdn.jsdelivr.net/npm/flowbite@3.1.2/dist/flowbite.min.js',
					crossorigin: "anonymous",
				},
			},
			{
				tag: 'link',
				attrs: {
					href: '', // https://cdn.jsdelivr.net/npm/bootstrap@5.3.7/dist/css/bootstrap.css
					rel: "stylesheet",
					crossorigin: "anonymous",
				},
			},
		],
		customCss: ['./src/styles/global.css'],
		social: [{ icon: 'github', label: 'GitHub', href: 'https://github.com/sf666/nextcp2' }],
		sidebar: [
			{ label: 'Installation', autogenerate: { directory: '/quick_install' }, },
			{ label: 'User Interface', autogenerate: { directory: '/user_interface' }, },
		],
	}), react(),],
});