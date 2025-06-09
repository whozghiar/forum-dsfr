// src/services/apiService.ts
import type { Forum } from '@/models/Forum'
import type { Sujet } from '@/models/Sujet'
import type { Message } from '@/models/Message'
import keycloak from './keycloak'

/**
 * Récupère la liste de tous les forums depuis l'API backend.
 * @returns {Promise<Forum[]>} Une promesse contenant la liste des forums.
 * @throws {Error} Si une erreur survient lors de l'appel à l'API.
 */
export async function getAllForums(): Promise<Forum[]> {
  console.log('Appel API /api/forums...')
  const response = await fetch('/api/forums', {
    headers: authHeaders()
  })
  if (!response.ok) throw new Error('Erreur lors du chargement des forums')
  return await response.json()
}

/**
 * Récupère la liste des sujets d'un forum spécifique depuis l'API backend.
 * @param {number} forumId - L'identifiant du forum pour lequel récupérer les sujets.
 * @returns {Promise<Sujet[]>} Une promesse contenant la liste des sujets du forum.
 * @throws {Error} Si une erreur survient lors de l'appel à l'API.
 */
export async function getSujetsByForumId(forumId: number): Promise<Forum> {
  console.log('Appel API /api/forums/id='+ forumId)
  const response = await fetch('/api/forums/'+ forumId, {
    headers: authHeaders()
  })
  if (!response.ok) throw new Error('Erreur lors du chargement des sujets')
  return await response.json()
}

/**
 * Crée un nouveau sujet dans un forum spécifique.
 * @param {number} forumId - L'identifiant du forum dans lequel créer le sujet.
 * @param {Object} dto - Un objet contenant les informations du sujet à créer.
 * @param {string} dto.titre - Le titre du sujet.
 * @param {string} dto.message - Le contenu du sujet.
 * @param {string} dto.auteurId - L'identifiant de l'auteur du sujet.
 * @returns {Promise<Sujet>} Une promesse contenant le sujet créé.
 * @throws {Error} Si une erreur survient lors de l'appel à l'API.
 */
export async function creerSujet(forumId: number, dto: {
  titre: string
  message: string
  auteurId: string
}): Promise<Sujet> {
  const response = await fetch('/api/forums/'+forumId+'/sujets/create', {
    method: 'POST',
    headers: { 'Content-Type': 'application/json', ...authHeaders() },
    body: JSON.stringify(dto)
  })
  if (!response.ok) throw new Error('Erreur lors de la création du sujet')
  return await response.json()
}

/**
 * Récupère la liste des messages d'un sujet spécifique depuis l'API backend.
 * @param {number} forumId - L'identifiant du forum.
 * @param {number} sujetId - L'identifiant du sujet.
 * @returns {Promise<Message[]>} Une promesse contenant la liste des messages du sujet.
 * @throws {Error} Si une erreur survient lors de l'appel à l'API.
 */
export async function getMessagesBySujetId(forumId: number, sujetId: number): Promise<Message[]> {
  console.log('Appel API /api/forums/'+ forumId +'/sujets/'+ sujetId + '/messages')
  const response = await fetch('/api/forums/'+ forumId +'/sujets/'+ sujetId + '/messages', {
    headers: authHeaders()
  })
  if (!response.ok) throw new Error('Erreur lors du chargement des messages')
  return await response.json()
}

/**
 * Crée un nouveau message dans un sujet spécifique.
 * @param {number} forumId - L'identifiant du forum.
 * @param {number} sujetId - L'identifiant du sujet.
 * @param {Object} dto - Un objet contenant les informations du message à créer.
 * @param {string} dto.contenu - Le contenu du message.
 * @param {number} dto.auteurId - L'identifiant de l'auteur du message.
 * @returns {Promise<Message>} Une promesse contenant le message créé.
 * @throws {Error} Si une erreur survient lors de l'appel à l'API.
 */
export async function creerMessage(forumId: number, sujetId: number, dto: {
  contenu: string
  auteurId: number
}): Promise<Message> {
  const response = await fetch('/api/forums/'+ forumId +'/sujets/'+ sujetId + '/messages/create', {
    method: 'POST',
    headers: { 'Content-Type': 'application/json', ...authHeaders() },
    body: JSON.stringify(dto)
  })
  if (!response.ok) throw new Error('Erreur lors de la création du message')
  return await response.json()
}

/**
 * Enregistre un nouvel utilisateur via l'API backend.
 * @param {Object} dto - Un objet contenant les informations de l'utilisateur à enregistrer.
 * @param {string} dto.pseudonyme - Le pseudonyme de l'utilisateur.
 * @param {string} dto.email - L'adresse email de l'utilisateur.
 * @param {string} dto.motDePasse - Le mot de passe de l'utilisateur.
 * @returns {Promise<void>} Une promesse résolue si l'enregistrement est réussi.
 * @throws {Error} Si une erreur survient lors de l'appel à l'API.
 */
export async function inscrireUtilisateur(dto: { pseudonyme: string; email: string; motDePasse: string }): Promise<void> {
  const response = await fetch('/api/auth/register', {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify(dto)
  })
  if (!response.ok) throw new Error('Erreur lors de l\'enregistrement')
}

/**
 * Connecte un utilisateur via l'API backend.
 * @param {Object} dto - Un objet contenant les informations de connexion de l'utilisateur.
 * @param {string} dto.pseudonyme - Le pseudonyme de l'utilisateur.
 * @param {string} dto.motDePasse - Le mot de passe de l'utilisateur.
 * @returns {Promise<Object>} Une promesse contenant les tokens d'accès, de rafraîchissement et d'identité.
 * @throws {Error} Si une erreur survient lors de l'appel à l'API.
 */
export async function connecterUtilisateur(dto: { pseudonyme: string; motDePasse: string }): Promise<{ accessToken: string; refreshToken: string; idToken: string }> {
  const response = await fetch('/api/auth/login', {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify(dto)
  })
  if (!response.ok) throw new Error('Erreur lors de la connexion')
  return await response.json()
}


/**
 * Génère les en-têtes d'autorisation pour les appels API.
 * @returns {Record<string, string>} Un objet contenant les en-têtes d'autorisation.
 */
function authHeaders(): Record<string, string> {
  if (keycloak.token) {
    return { Authorization: 'Bearer ' + keycloak.token }
  }
  return {}
}

