import Keycloak, { type KeycloakInitOptions } from 'keycloak-js'

const keycloak = new Keycloak({
  url: 'http://localhost:8083',
  realm: 'forum-dsfr',
  clientId: 'forum-dsfr-frontend'
})

export function initKeycloak(options: KeycloakInitOptions = {}) {
  // Vérifie si l'utilisateur est déjà authentifié
  if (keycloak.authenticated) {
    console.log('Utilisateur déjà authentifié')
    return Promise.resolve(true)
  }
  return keycloak.init({ onLoad: 'check-sso', pkceMethod: 'S256', ...options })
}

export default keycloak
