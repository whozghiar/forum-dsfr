<template>
  <DsfrBreadcrumb :links="filAriane" />
  <main class="fr-pt-md-14v" role="main" id="content">
    <div class="fr-container fr-container--fluid fr-mb-md-14v">
      <div class="fr-grid-row fr-grid-row-gutters fr-grid-row--center">
        <div class="fr-col-12 fr-col-md-8 fr-col-lg-6">
          <div class="fr-container fr-background-alt--grey fr-px-md-0 fr-py-10v fr-py-md-14v">
            <div class="fr-grid-row fr-grid-row-gutters fr-grid-row--center">
              <div class="fr-col-12 fr-col-md-9 fr-col-lg-8">
                <h1>Connexion</h1>

                <DsfrFieldset legend="Se connecter avec son compte">
                  <DsfrInputGroup
                    v-model="pseudonyme"
                    label="Identifiant"
                    type="text"
                    hint="Format attendu : nom@domaine.fr"
                    autocomplete="username"
                    required
                  />

                  <DsfrInputGroup
                    v-model="motDePasse"
                    label="Mot de passe"
                    type="password"
                    autocomplete="current-password"
                    required
                  />

                  <div class="fr-checkbox-group fr-checkbox-group--sm">
                    <input id="souvenir" type="checkbox" v-model="seSouvenir" />
                    <label for="souvenir">Se souvenir de moi</label>
                  </div>

                  <p class="fr-link">
                    <a href="#">Mot de passe oublié ?</a>
                  </p>

                  <DsfrButton @click="seConnecter">Se connecter</DsfrButton>
                </DsfrFieldset>

                <hr class="fr-mt-4v" />
                <h2>Vous n’avez pas de compte ?</h2>
                <DsfrButton secondary @click="creerCompte">Créer un compte</DsfrButton>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </main>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import {DsfrButton, DsfrBreadcrumb} from '@gouvminint/vue-dsfr'
import { connecterUtilisateur } from '@/services/apiService'
import { initKeycloak } from '@/services/keycloak'
import { useRouter } from 'vue-router'

const filAriane = [
  { to: '/', text: 'Accueil' },
  { to: '/connexion', text: 'Connexion' }
]
const pseudonyme = ref('')
const motDePasse = ref('')
const seSouvenir = ref(false)
const router = useRouter()

function creerCompte() {
  router.push('/inscription')
}

async function seConnecter() {
  const tokens = await connecterUtilisateur({ pseudonyme: pseudonyme.value, motDePasse: motDePasse.value })
  await initKeycloak({ token: tokens.accessToken, refreshToken: tokens.refreshToken, idToken: tokens.idToken })
  router.push('/')
}
</script>
