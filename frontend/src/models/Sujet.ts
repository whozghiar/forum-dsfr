export interface Sujet {
  id: number
  titre: string
  dateCreation: string
  auteur: {
    id: number
    pseudo: string
  }
  nbMessages: number
  dernierMessage?: string
}