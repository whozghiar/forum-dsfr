/**
 * Représente un sujet récupéré depuis l'API backend.
 * Contient l'identifiant, le titre, la date de création, l'auteur et le nombre de messages
 */
export interface Sujet {
  idSujet: number
  titre: string
  dateCreation: string
  auteur: {
    id: number
    pseudo: string
  }
  nbMessages: number
  estEpingle: boolean
}