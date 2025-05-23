// src/services/apiService.ts
import type { Forum } from '@/models/Forum'
import type {Sujet} from "@/models/Sujet";
import type {Message} from "@/models/Message";

/**
 * Récupère la liste de tous les forums depuis l'API backend.
 * @returns {Promise<Forum[]>} Une promesse contenant la liste des forums.
 * @throws {Error} Si une erreur survient lors de l'appel à l'API.
 */
export async function getAllForums(): Promise<Forum[]> {
  console.log('Appel API /api/forums...')
  const response = await fetch('/api/forums')
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
  const response = await fetch('/api/forums/'+ forumId)
  if (!response.ok) throw new Error('Erreur lors du chargement des sujets')
  return await response.json()
}

/**
 * Crée un nouveau sujet dans un forum spécifique.
 * @param {number} forumId - L'identifiant du forum dans lequel créer le sujet.
 * @param dto - Un objet contenant les informations du sujet à créer.
 */
export async function creerSujet(forumId: number, dto: {
  titre: string
  message: string
  auteurId: string
}): Promise<Sujet> {
  const response = await fetch('/api/forums/'+forumId+'/sujets/create', {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify(dto)
  })
  if (!response.ok) throw new Error('Erreur lors de la création du sujet')
  return await response.json()
}

/**
 * Récupère la liste des messages d'un sujet spécifique depuis l'API backend.
 * @param forumId
 * @param sujetId
 */
export async function getMessagesBySujetId(forumId: number, sujetId: number): Promise<Message[]> {
  console.log('Appel API /api/forums/'+ forumId +'/sujets/'+ sujetId + '/messages')
  const response = await fetch('/api/forums/'+ forumId +'/sujets/'+ sujetId + '/messages')
  if (!response.ok) throw new Error('Erreur lors du chargement des messages')
  return await response.json()
}

/**
 * Crée un nouveau message dans un sujet spécifique.
 * @param forumId
 * @param sujetId
 * @param dto
 */
export async function creerMessage(forumId: number, sujetId: number, dto: {
  contenu: string
  auteurId: number
}): Promise<Message> {
  const response = await fetch('/api/forums/'+ forumId +'/sujets/'+ sujetId + '/messages/create', {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify(dto)
  })
  if (!response.ok) throw new Error('Erreur lors de la création du message')
  return await response.json()
}

