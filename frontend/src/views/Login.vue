<template>
  <section class="fr-container fr-mt-6w">
    <h1 class="fr-h2">Connexion</h1>
    <DsfrButton @click="login">Se connecter avec Keycloak</DsfrButton>
  </section>
</template>

<script setup lang="ts">
import { DsfrButton } from '@gouvminint/vue-dsfr'
import keycloak from '@/services/keycloak'

function login() {
  keycloak.login({ redirectUri: window.location.origin })
}
</script>
