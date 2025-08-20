// @ts-check
import { defineConfig } from 'astro/config';
import starlight from '@astrojs/starlight';
import tailwindcss from '@tailwindcss/vite'

import react from '@astrojs/react';

// https://astro.build/config
export default defineConfig({
	base: '/nextcp2/',
	vite: {
		plugins: [tailwindcss()],
	},
	integrations: [starlight({
		title: 'NextCP/2 Documentation',
		head: [
			{
				tag: 'link',
				attrs: {
					href: 'https://fonts.googleapis.com/css2?family=Material+Symbols+Outlined:opsz,wght,FILL,GRAD@20..48,100..700,0..1,-50..200',
					rel: "stylesheet",
					crossorigin: "anonymous",
				},
			},
			{
				tag: 'link',
				attrs: {
					href: 'https://fonts.googleapis.com/css2?family=Material+Symbols+Sharp:opsz,wght,FILL,GRAD@20..48,100..700,0..1,-50..200',
					rel: "stylesheet",
					crossorigin: "anonymous",
				},
			},
			{
				tag: 'script',
				attrs: {
					src: 'https://cdn.jsdelivr.net/npm/flowbite@3.1.2/dist/flowbite.min.js',
					crossorigin: "anonymous",
				},
			},
		],
		customCss: ['./src/styles/global.css'],
		social: [{ icon: 'github', label: 'GitHub', href: 'https://github.com/sf666/nextcp2' }],
		sidebar: [
			{ label: 'Overview', items: [ 
				'overview/overview',
				'overview/issue',
			]},
			{ label: 'Installation', items: [ 
				'quick_install/quick_install',
				'quick_install/docker',
			]},
			{ label: 'User Interface', items: [ 
				'user_interface/music_library',
				'user_interface/player_queue',
				'user_interface/now_listening',
				'user_interface/radio',
				'user_interface/my_albums',
				'user_interface/app_settings',
			]},
			{ label: 'Integration', items: [ 
				'integration/rest_api',
				'integration/openhab',
			]},
		],
	}), react(),],
});