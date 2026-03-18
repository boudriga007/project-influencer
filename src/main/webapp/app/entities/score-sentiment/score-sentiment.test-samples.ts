import { IScoreSentiment, NewScoreSentiment } from './score-sentiment.model';

export const sampleWithRequiredData: IScoreSentiment = {
  id: 11892,
};

export const sampleWithPartialData: IScoreSentiment = {
  id: 21323,
  ratioNegatif: 17054.61,
  ratioNeutre: 19537.37,
  scoreSentiment: 4425.38,
};

export const sampleWithFullData: IScoreSentiment = {
  id: 32149,
  ratioPositif: 25583.03,
  ratioNegatif: 28920.96,
  ratioNeutre: 20354.72,
  scoreSentiment: 11207.02,
};

export const sampleWithNewData: NewScoreSentiment = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
