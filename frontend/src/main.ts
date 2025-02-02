import { createApp } from 'vue'
import { createRouter, createWebHistory } from 'vue-router'
import axios from 'axios'
import App from './App.vue'
import Home from './views/Home.vue'
import History from './views/History.vue'

axios.defaults.baseURL = import.meta.env.VITE_API_URL || 'http://localhost:9000'

const router = createRouter({
  history: createWebHistory(),
  routes: [
    {
      path: '/',
      name: 'home',
      component: Home
    },
    {
      path: '/history',
      name: 'history',
      component: History
    }
  ]
})

const app = createApp(App)
app.use(router)
app.mount('#app')
