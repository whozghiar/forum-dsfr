import { createApp } from 'vue'
import App from './App.vue'
import router from './router/index'
import keycloak, { initKeycloak } from './services/keycloak'
import '@gouvfr/dsfr/dist/core/core.main.min.css'

import '@gouvfr/dsfr/dist/component/component.main.min.css'
import '@gouvfr/dsfr/dist/utility/utility.main.min.css'

import '@gouvminint/vue-dsfr/styles'

import '@gouvfr/dsfr/dist/scheme/scheme.min.css'
import '@gouvfr/dsfr/dist/utility/icons/icons.min.css'

import './main.css'

initKeycloak({ onLoad: 'check-sso', pkceMethod: 'S256' })
  .then((authenticated) => {
    if (authenticated) {
      console.log("Token Keycloak :", keycloak.token)
      console.log("Token d'actualisation Keycloak :", keycloak.refreshToken)
      console.log("Utilisateur Keycloak :", keycloak.tokenParsed)
    } else {
      console.warn("Utilisateur non authentifié")
    }

    createApp(App)
      .use(router)
      .mount('#app')
  })
  .catch((err) => {
    console.error('Erreur lors de l\'initialisation de Keycloak :', err)
  })