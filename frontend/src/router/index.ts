import { createRouter, createWebHistory } from 'vue-router'

import AboutUs from '../views/AboutUs.vue'
import Home from '../views/AppHome.vue'
import ForumList from "@/components/ForumList.vue";
import TopicList from "@/components/TopicList.vue";

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
    path: '/forums',
    name: 'Forums',
    component: ForumList,
  },
  { path: '/forums/:forumId/sujets', 
    name: 'Sujets', 
    component: TopicList }
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
