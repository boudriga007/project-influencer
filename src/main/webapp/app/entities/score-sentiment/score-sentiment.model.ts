import { IInfluenceur } from 'app/entities/influenceur/influenceur.model';

export interface IScoreSentiment {
  id: number;
  ratioPositif?: number | null;
  ratioNegatif?: number | null;
  ratioNeutre?: number | null;
  scoreSentiment?: number | null;
  influenceur?: Pick<IInfluenceur, 'id'> | null;
}

export type NewScoreSentiment = Omit<IScoreSentiment, 'id'> & { id: null };
