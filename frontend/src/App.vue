<script setup lang="ts">
import { useScheme } from '@gouvminint/vue-dsfr'
import useToaster from './composables/use-toaster'
import { computed, onMounted, ref } from 'vue'
import keycloak, { logoutKeycloak } from './services/keycloak'
import router from './router'

useScheme()

const toaster = useToaster()

const serviceTitle = 'Service'
const serviceDescription = 'Description du service'
const logoText = ['Ministère', 'de l’intérieur']

const searchQuery = ref('')

const isAuthenticated = ref(false)
const username = ref('')

onMounted(() => {
  if (keycloak.authenticated) {
    isAuthenticated.value = true
    username.value = keycloak.tokenParsed?.preferred_username ?? ''
  }
  keycloak.onAuthSuccess = () => {
    isAuthenticated.value = true
    username.value = keycloak.tokenParsed?.preferred_username ?? ''
  }
  keycloak.onAuthLogout = () => {
    isAuthenticated.value = false
    username.value = ''
  }
})

function deconnecter() {
  logoutKeycloak().then(() => {
    router.push('/connexion')
  })
}

const quickLinks = computed(() => {
  const links = [
    {
      label: 'Home',
      to: '/',
      icon: 'ri-home-4-line',
      iconAttrs: { color: 'var(--red-marianne-425-625)' },
    },
    {
      label: 'Forums',
      to: '/',
      icon: 'fr-icon-team-fill',
    },
  ]
  if (isAuthenticated.value) {
    links.push({ label: username.value, icon: 'fr-icon-user-fill' })
    links.push({ label: 'Déconnexion', onClick: deconnecter, icon: 'ri-logout-box-r-line' })
  } else {
    links.push({ label: 'Connexion', to: '/connexion', icon: 'fr-icon-user-fill' })
    links.push({ label: 'Inscription', to: '/inscription', icon: 'fr-icon-user-add-fill' })
  }
  links.push({ label: 'À propos', to: '/a-propos', icon: 'ri-question-mark', iconLeft: true })
  return links
})
</script>

<template>
  <DsfrHeader
    v-model="searchQuery"
    :service-title="serviceTitle"
    :service-description="serviceDescription"
    :logo-text="logoText"
    :quick-links="quickLinks"
    show-search
  />

  <div class="fr-container  fr-mt-3w  fr-mt-md-5w  fr-mb-5w">
    <router-view />
  </div>
</template>
