<template>
  <section class="fr-container fr-mt-6w">
    <h1 class="fr-h2">Connexion</h1>
    <form @submit.prevent="submit">
      <DsfrInput v-model="username" label="Identifiant" required />
      <DsfrInput v-model="password" label="Mot de passe" type="password" required />
      <DsfrButton type="submit">Se connecter</DsfrButton>
    </form>
  </section>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import { DsfrInput, DsfrButton } from '@gouvminint/vue-dsfr'
import { loginUser } from '@/services/apiService'
import { initKeycloak } from '@/services/keycloak'
import { useRouter } from 'vue-router'

const username = ref('')
const password = ref('')
const router = useRouter()

async function submit() {
  const tokens = await loginUser({ username: username.value, password: password.value })
  await initKeycloak({ token: tokens.accessToken, refreshToken: tokens.refreshToken, idToken: tokens.idToken })
  router.push('/')
}
</script>
