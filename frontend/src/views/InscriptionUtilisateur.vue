<template>
  <div class="fr-container fr-my-4w">
    <DsfrBreadcrumb :links="filAriane" />
    <DsfrStepper :steps="['Vos informations','Confirmation']" :current-step="etapeCourante" />

    <form @submit.prevent="etapeCourante === 1 ? etapeSuivante() : soumettreFormulaire()">
      <DsfrFieldset legend="Vos informations personnelles" legendClass="fr-h5">
        <DsfrInputGroup
          v-model="form.nom_utilisateur"
          label="Pseudonyme"
          label-visible
          hint="Ex. superUtilisateurDu91"
          :error-message="errors.nom_utilisateur"
          @blur="validateUsername"
          required
        />
        <DsfrInputGroup
          v-model="form.email"
          label="Adresse courriel"
          type="email"
          label-visible
          hint="prenom.nom@example.com"
          :error-message="errors.email"
          @blur="validateEmail"
          required
        />
        <DsfrInputGroup
          v-model="form.password"
          label="Mot de passe"
          type="password"
          label-visible
          hint="8 caractères, une majuscule, minuscule, chiffre"
          :error-message="errors.password"
          @blur="validatePassword"
          required
        />
        
        <DsfrInputGroup
          v-model="form.confirmPassword"
          label="Confirmer le mot de passe"
          type="password"
          label-visible
          :error-message="errors.confirmPassword"
          @blur="validateConfirmPassword"
          required
        />
      </DsfrFieldset>

      <div class="fr-mt-2w">
        <DsfrCheckbox
          name="Accepter Les termes"
          v-model="form.acceptTerms"
          label="J’accepte les conditions générales"
          :error-message="errors.acceptTerms"
          @change="validateAcceptTerms"
          required
        />
      </div>

      <DsfrNotice
        v-if="erreurGlobal"
        type="alert"
        :title="erreurGlobal.titre"
        :description="erreurGlobal.texte"
        class="fr-mt-2w"
      />

      <div class="fr-grid-row fr-justify-end fr-mt-3v">
        <div class="fr-col-auto">
          <DsfrButton v-if="etapeCourante === 2" type="button" label="Précédent" secondary @click="etapePrecedente" />
          <DsfrButton v-if="etapeCourante === 1" type="button" label="Suivant" secondary @click="etapeSuivante" />
          <DsfrButton v-if="etapeCourante === 2" type="submit" label="Créer mon compte" />
        </div>
      </div>
    </form>

    <DsfrNotice
      v-if="isFormulaireSoumis"
      title="Inscription réussie"
      description="Votre compte a bien été créé."
      class="fr-mt-4v"
    />
  </div>
</template>

<script setup lang="ts">
import { reactive, ref } from 'vue'
import {
  DsfrBreadcrumb,
  DsfrStepper,
  DsfrFieldset,
  DsfrInputGroup,
  DsfrCheckbox,
  DsfrButton,
  DsfrNotice
} from '@gouvminint/vue-dsfr'
import useInscriptionValidation from '@/composables/use-inscription-validation'
import { inscrireUtilisateur } from '@/services/apiService'

const filAriane = [
  { to: '/', text: 'Accueil' },
  { to: '/inscription', text: 'Créer un compte' }
]

const form = reactive({
  nom_utilisateur: '',
  email: '',
  password: '',
  confirmPassword: '',
  acceptTerms: false
})

// Validation centralisée du formulaire d'inscription
const {
  errors,
  validateUsername,
  validateEmail,
  validatePassword,
  validateConfirmPassword,
  validateAcceptTerms,
  validateAll,
  isValid
} = useInscriptionValidation(form)

// Erreur globale (submit)
const erreurGlobal = ref<{titre:string,texte:string}|null>(null)
const isFormulaireSoumis = ref(false)
const etapeCourante = ref(1)

// Navigation étapes
function etapeSuivante() {
  validateAll()
  if (isValid()) {
    erreurGlobal.value = null
    etapeCourante.value = 2
  } else {
    erreurGlobal.value = {
      titre: 'Formulaire invalide',
      texte: 'Veuillez corriger les erreurs'
    }
  }
}

function etapePrecedente() {
  erreurGlobal.value = null
  etapeCourante.value = 1
}

// Soumission
async function soumettreFormulaire() {
  validateAll()
  if (!isValid()) {
    erreurGlobal.value = { titre: 'Formulaire invalide', texte: 'Veuillez corriger les erreurs' }
    return
  }

  try {
    await inscrireUtilisateur({
      pseudonyme: form.nom_utilisateur,
      email: form.email,
      motDePasse: form.password
    })
    isFormulaireSoumis.value = true
  } catch (e: any) {
    erreurGlobal.value = { titre: 'Erreur serveur', texte: e.message || 'Réessayez plus tard.' }
  }
}
</script>
