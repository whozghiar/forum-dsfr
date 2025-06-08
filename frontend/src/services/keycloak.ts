import Keycloak, { type KeycloakInitOptions } from 'keycloak-js'

const keycloak = new Keycloak({
  url: 'http://localhost:8083',
  realm: 'forum-dsfr',
  clientId: 'frontend'
})

export function initKeycloak(options: KeycloakInitOptions = {}) {
  return keycloak.init({ onLoad: 'check-sso', pkceMethod: 'S256', ...options })
}

export default keycloak
