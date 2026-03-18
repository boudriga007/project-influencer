import { IInfluenceur, NewInfluenceur } from './influenceur.model';

export const sampleWithRequiredData: IInfluenceur = {
  id: 19693,
  nom: 'pace whoa minus',
};

export const sampleWithPartialData: IInfluenceur = {
  id: 15590,
  nom: 'admired up trash',
  username: 'fortunately',
  photoUrl: 'steep whenever whoa',
  plateforme: 'TIKTOK',
  categorie: 'HUMOUR',
  scoreGlobal: 4042.24,
};

export const sampleWithFullData: IInfluenceur = {
  id: 30888,
  nom: 'at',
  username: 'lively',
  photoUrl: 'pack requite satirize',
  bio: 'shrilly rebound',
  plateforme: 'TIKTOK',
  categorie: 'SPORT',
  scoreGlobal: 8119.03,
};

export const sampleWithNewData: NewInfluenceur = {
  nom: 'loudly',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
