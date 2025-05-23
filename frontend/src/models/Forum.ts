import type {Sujet} from "@/models/Sujet";

/**
 * Représente un forum récupéré depuis l'API backend.
 * Contient le titre, la description et la liste des sujets liés.
 */
export interface Forum {
  idForum: number
  titre: string
  description: string
  sujets: Sujet[]
}