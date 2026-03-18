import { IInfluenceur } from 'app/entities/influenceur/influenceur.model';

export interface IScoreReach {
  id: number;
  nbFollowers?: number | null;
  engagementRate?: number | null;
  reachEstime?: number | null;
  scoreReach?: number | null;
  influenceur?: Pick<IInfluenceur, 'id'> | null;
}

export type NewScoreReach = Omit<IScoreReach, 'id'> & { id: null };
