import { vi, beforeEach } from 'vitest'
import { createRouter, createWebHistory } from 'vue-router'
import Home from '../../views/Home.vue'
import History from '../../views/History.vue'

// Mock Vue Router
export const router = createRouter({
  history: createWebHistory(),
  routes: [
    { path: '/', component: Home },
    { path: '/history', component: History }
  ]
})

// Setup global mocks before each test

// Setup global mocks before each test
beforeEach(() => {
  vi.clearAllMocks()
})
