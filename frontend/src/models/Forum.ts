import type {Sujet} from "@/models/Sujet";

export interface Forum {
  idForum: number
  titre: string
  description: string
  sujets: Sujet[]
}