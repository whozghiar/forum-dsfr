import { createRouter, createWebHistory } from 'vue-router'

import AboutUs from '../views/AboutUs.vue'
import Home from '../views/AppHome.vue'
import Forum from '../views/Forum.vue'

const MAIN_TITLE = 'Gabarit de démarrage VueDsfr'

const routes = [
  {
    path: '/',
    name: 'Home',
    component: Home,
  },
  {
    path: '/a-propos',
    name: 'About',
    component: AboutUs,
  },
  {
    path: '/forum',
    name: 'Forum',
    component: Forum,
  }
]

const router = createRouter({
  history: createWebHistory(import.meta.env?.BASE_URL || ''),
  routes,
})

router.beforeEach((to) => { // Cf. https://github.com/vueuse/head pour des transformations avancées de Head
  const specificTitle = to.meta.title ? `${to.meta.title} - ` : ''
  document.title = `${specificTitle}${MAIN_TITLE}`
})

export default router
