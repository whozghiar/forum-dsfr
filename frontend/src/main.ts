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

initKeycloak()
  .catch((err) => {
    console.error('Erreur lors de l\'initialisation de Keycloak :', err)
  })
  .finally(() => {
    createApp(App)
      .use(router)
      .mount('#app')
  })