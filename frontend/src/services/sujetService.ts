// src/services/sujetService.ts
import type { Sujet } from '@/models/Sujet'

export async function creerSujet(forumId: number, dto: {
  titre: string
  contenu: string
  auteurId: number
}): Promise<Sujet> {
  const response = await fetch('/api/forums/'+forumId+'/sujets/create', {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify(dto)
  })
  if (!response.ok) throw new Error('Erreur lors de la création du sujet')
  return await response.json()
}
