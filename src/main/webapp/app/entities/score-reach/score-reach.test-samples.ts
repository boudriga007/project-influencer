import { IScoreReach, NewScoreReach } from './score-reach.model';

export const sampleWithRequiredData: IScoreReach = {
  id: 19919,
};

export const sampleWithPartialData: IScoreReach = {
  id: 5037,
  reachEstime: 10911.25,
  scoreReach: 18842.84,
};

export const sampleWithFullData: IScoreReach = {
  id: 32467,
  nbFollowers: 699,
  engagementRate: 20933.53,
  reachEstime: 9735.03,
  scoreReach: 7004.67,
};

export const sampleWithNewData: NewScoreReach = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
