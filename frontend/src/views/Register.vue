<template>
  <section class="fr-container fr-mt-6w">
    <h1 class="fr-h2">Créer un compte</h1>
    <form @submit.prevent="register">
      <DsfrInput v-model="username" label="Nom d'utilisateur" required />
      <DsfrInput v-model="email" label="Email" type="email" required />
      <DsfrInput v-model="password" label="Mot de passe" type="password" required />
      <DsfrButton type="submit">S'enregistrer</DsfrButton>
    </form>
  </section>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import { DsfrInput, DsfrButton } from '@gouvminint/vue-dsfr'
import { registerUser } from '@/services/apiService'

const username = ref('')
const email = ref('')
const password = ref('')

async function register() {
  await registerUser({ username: username.value, email: email.value, password: password.value })
}
</script>
