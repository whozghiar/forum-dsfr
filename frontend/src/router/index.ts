import { createRouter, createWebHistory } from 'vue-router'

import AboutUs from '../views/AboutUs.vue'
import Home from '../views/AppHome.vue'
import Login from '../views/Login.vue'
import Register from '../views/Register.vue'
import ForumList from "@/components/Forum/ForumList.vue";
import SujetList from "@/components/Sujet/SujetList.vue";
import Sujet from "@/components/Sujet/Sujet.vue";

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
    path: '/connexion',
    name: 'Login',
    component: Login,
  },
  {
    path: '/enregistrement',
    name: 'Register',
    component: Register,
  },
  {
    path: '/forums',
    name: 'Forums',
    component: ForumList,
  },
  { path: '/forums/:forumId/sujets', 
    name: 'Sujets', 
    component: SujetList 
  },
  {
    path: '/forums/:forumId/sujets/:sujetId/messages',
    name: 'Liste des messages',
    component: Sujet
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
