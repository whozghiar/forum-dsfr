<script setup lang="ts">
import { useScheme } from '@gouvminint/vue-dsfr'
import useToaster from './composables/use-toaster'

useScheme()

const toaster = useToaster()

const serviceTitle = 'Service'
const serviceDescription = 'Description du service'
const logoText = ['Ministère', 'de l’intérieur']

const quickLinks = [
  {
    label: 'Home',
    to: '/',
    icon: 'ri-home-4-line',
    iconAttrs: { color: 'var(--red-marianne-425-625)' },
  },
  {
    label: 'Forums',
    to: '/forums',
    icon: 'fr-icon-team-fill',
  },
  {
    label: "Connexion",
    to: '/connexion',
    icon: 'fr-icon-user-fill',
  },
  {
    label: "Inscription",
    to: '/inscription',
    icon: 'fr-icon-user-add-fill',
  },
  {
    label: 'À propos',
    to: '/a-propos',
    icon: 'ri-question-mark',
    iconLeft: true,
  }
]
const searchQuery = ref('')
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
