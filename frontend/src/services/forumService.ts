// src/services/forumService.ts
import type { Forum } from '@/models/Forum'
import type {Sujet} from "@/models/Sujet";


export async function getAllForums(): Promise<Forum[]> {
  console.log('Appel API /api/forums...')
  const response = await fetch('/api/forums')
  if (!response.ok) throw new Error('Erreur lors du chargement des forums')
  return await response.json()
}

export async function getSujetsByForumId(forumId: number): Promise<Sujet[]> {
  console.log('Appel API /api/forums/id='+ forumId)
  const response = await fetch('/api/forums/'+ forumId)
  if (!response.ok) throw new Error('Erreur lors du chargement des sujets')
  return await response.json()
}
