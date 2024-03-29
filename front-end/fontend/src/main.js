import {
	createSSRApp
} from "vue";
import uviewPlus from 'uview-plus'

import { pinia } from "./store";
import App from "./App.vue";

import 'uno.css'
import './style.css'

export function createApp() {
	const app = createSSRApp(App);
	app.use(uviewPlus)
	app.use(pinia);
	return {
		app,
	};
}