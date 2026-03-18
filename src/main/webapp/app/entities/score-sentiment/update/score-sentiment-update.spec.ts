import { beforeEach, describe, expect, it, vitest } from 'vitest';
import { HttpResponse } from '@angular/common/http';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';

import { Subject, from, of } from 'rxjs';

import { IInfluenceur } from 'app/entities/influenceur/influenceur.model';
import { InfluenceurService } from 'app/entities/influenceur/service/influenceur.service';
import { IScoreSentiment } from '../score-sentiment.model';
import { ScoreSentimentService } from '../service/score-sentiment.service';

import { ScoreSentimentFormService } from './score-sentiment-form.service';
import { ScoreSentimentUpdate } from './score-sentiment-update';

describe('ScoreSentiment Management Update Component', () => {
  let comp: ScoreSentimentUpdate;
  let fixture: ComponentFixture<ScoreSentimentUpdate>;
  let activatedRoute: ActivatedRoute;
  let scoreSentimentFormService: ScoreSentimentFormService;
  let scoreSentimentService: ScoreSentimentService;
  let influenceurService: InfluenceurService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [
        provideHttpClientTesting(),
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    });

    fixture = TestBed.createComponent(ScoreSentimentUpdate);
    activatedRoute = TestBed.inject(ActivatedRoute);
    scoreSentimentFormService = TestBed.inject(ScoreSentimentFormService);
    scoreSentimentService = TestBed.inject(ScoreSentimentService);
    influenceurService = TestBed.inject(InfluenceurService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should call influenceur query and add missing value', () => {
      const scoreSentiment: IScoreSentiment = { id: 30345 };
      const influenceur: IInfluenceur = { id: 16800 };
      scoreSentiment.influenceur = influenceur;

      const influenceurCollection: IInfluenceur[] = [{ id: 16800 }];
      vitest.spyOn(influenceurService, 'query').mockReturnValue(of(new HttpResponse({ body: influenceurCollection })));
      const expectedCollection: IInfluenceur[] = [influenceur, ...influenceurCollection];
      vitest.spyOn(influenceurService, 'addInfluenceurToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ scoreSentiment });
      comp.ngOnInit();

      expect(influenceurService.query).toHaveBeenCalled();
      expect(influenceurService.addInfluenceurToCollectionIfMissing).toHaveBeenCalledWith(influenceurCollection, influenceur);
      expect(comp.influenceursCollection()).toEqual(expectedCollection);
    });

    it('should update editForm', () => {
      const scoreSentiment: IScoreSentiment = { id: 30345 };
      const influenceur: IInfluenceur = { id: 16800 };
      scoreSentiment.influenceur = influenceur;

      activatedRoute.data = of({ scoreSentiment });
      comp.ngOnInit();

      expect(comp.influenceursCollection()).toContainEqual(influenceur);
      expect(comp.scoreSentiment).toEqual(scoreSentiment);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<IScoreSentiment>();
      const scoreSentiment = { id: 17421 };
      vitest.spyOn(scoreSentimentFormService, 'getScoreSentiment').mockReturnValue(scoreSentiment);
      vitest.spyOn(scoreSentimentService, 'update').mockReturnValue(saveSubject);
      vitest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ scoreSentiment });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(scoreSentiment);
      saveSubject.complete();

      // THEN
      expect(scoreSentimentFormService.getScoreSentiment).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(scoreSentimentService.update).toHaveBeenCalledWith(expect.objectContaining(scoreSentiment));
      expect(comp.isSaving()).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<IScoreSentiment>();
      const scoreSentiment = { id: 17421 };
      vitest.spyOn(scoreSentimentFormService, 'getScoreSentiment').mockReturnValue({ id: null });
      vitest.spyOn(scoreSentimentService, 'create').mockReturnValue(saveSubject);
      vitest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ scoreSentiment: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(scoreSentiment);
      saveSubject.complete();

      // THEN
      expect(scoreSentimentFormService.getScoreSentiment).toHaveBeenCalled();
      expect(scoreSentimentService.create).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<IScoreSentiment>();
      const scoreSentiment = { id: 17421 };
      vitest.spyOn(scoreSentimentService, 'update').mockReturnValue(saveSubject);
      vitest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ scoreSentiment });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(scoreSentimentService.update).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareInfluenceur', () => {
      it('should forward to influenceurService', () => {
        const entity = { id: 16800 };
        const entity2 = { id: 28452 };
        vitest.spyOn(influenceurService, 'compareInfluenceur');
        comp.compareInfluenceur(entity, entity2);
        expect(influenceurService.compareInfluenceur).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
