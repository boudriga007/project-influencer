import { Categorie } from 'app/entities/enumerations/categorie.model';
import { Plateforme } from 'app/entities/enumerations/plateforme.model';

export interface IInfluenceur {
  id: number;
  nom?: string | null;
  username?: string | null;
  photoUrl?: string | null;
  bio?: string | null;
  plateforme?: keyof typeof Plateforme | null;
  categorie?: keyof typeof Categorie | null;
  scoreGlobal?: number | null;
}

export type NewInfluenceur = Omit<IInfluenceur, 'id'> & { id: null };
