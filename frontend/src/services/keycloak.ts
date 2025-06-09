import Keycloak, { type KeycloakInitOptions } from 'keycloak-js'

const keycloak = new Keycloak({
  url: 'http://localhost:8083',
  realm: 'forum-dsfr',
  clientId: 'forum-dsfr-frontend'
})

const TOKEN_KEY = 'kc_token'
const REFRESH_KEY = 'kc_refresh'
const ID_KEY = 'kc_id'

let initialized = false

function saveTokens() {
  if (keycloak.token) {
    sessionStorage.setItem(TOKEN_KEY, keycloak.token)
    if (keycloak.refreshToken) {
      sessionStorage.setItem(REFRESH_KEY, keycloak.refreshToken)
    }
    if (keycloak.idToken) {
      sessionStorage.setItem(ID_KEY, keycloak.idToken)
    }
  }
}

function clearTokens() {
  sessionStorage.removeItem(TOKEN_KEY)
  sessionStorage.removeItem(REFRESH_KEY)
  sessionStorage.removeItem(ID_KEY)
}

export async function initKeycloak(options: KeycloakInitOptions = {}) {
  if (initialized) {
    if (options.token) {
      keycloak.token = options.token as string
      keycloak.refreshToken = options.refreshToken
      keycloak.idToken = options.idToken
      saveTokens()
      return true
    }
    return keycloak.authenticated ?? false
  }

  const storedToken = sessionStorage.getItem(TOKEN_KEY) || undefined
  const storedRefresh = sessionStorage.getItem(REFRESH_KEY) || undefined
  const storedId = sessionStorage.getItem(ID_KEY) || undefined

  const opts: KeycloakInitOptions = {
    onLoad: 'check-sso',
    pkceMethod: 'S256',
    ...options,
    token: options.token ?? storedToken,
    refreshToken: options.refreshToken ?? storedRefresh,
    idToken: options.idToken ?? storedId
  }

  const authenticated = await keycloak.init(opts)
  initialized = true
  if (authenticated) {
    saveTokens()
  } else {
    clearTokens()
  }
  return authenticated
}

export async function logoutKeycloak() {
  clearTokens()
  await keycloak.logout()
}

export default keycloak
