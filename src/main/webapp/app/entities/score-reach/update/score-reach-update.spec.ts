import { beforeEach, describe, expect, it, vitest } from 'vitest';
import { HttpResponse } from '@angular/common/http';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';

import { Subject, from, of } from 'rxjs';

import { IInfluenceur } from 'app/entities/influenceur/influenceur.model';
import { InfluenceurService } from 'app/entities/influenceur/service/influenceur.service';
import { IScoreReach } from '../score-reach.model';
import { ScoreReachService } from '../service/score-reach.service';

import { ScoreReachFormService } from './score-reach-form.service';
import { ScoreReachUpdate } from './score-reach-update';

describe('ScoreReach Management Update Component', () => {
  let comp: ScoreReachUpdate;
  let fixture: ComponentFixture<ScoreReachUpdate>;
  let activatedRoute: ActivatedRoute;
  let scoreReachFormService: ScoreReachFormService;
  let scoreReachService: ScoreReachService;
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

    fixture = TestBed.createComponent(ScoreReachUpdate);
    activatedRoute = TestBed.inject(ActivatedRoute);
    scoreReachFormService = TestBed.inject(ScoreReachFormService);
    scoreReachService = TestBed.inject(ScoreReachService);
    influenceurService = TestBed.inject(InfluenceurService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should call influenceur query and add missing value', () => {
      const scoreReach: IScoreReach = { id: 27588 };
      const influenceur: IInfluenceur = { id: 16800 };
      scoreReach.influenceur = influenceur;

      const influenceurCollection: IInfluenceur[] = [{ id: 16800 }];
      vitest.spyOn(influenceurService, 'query').mockReturnValue(of(new HttpResponse({ body: influenceurCollection })));
      const expectedCollection: IInfluenceur[] = [influenceur, ...influenceurCollection];
      vitest.spyOn(influenceurService, 'addInfluenceurToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ scoreReach });
      comp.ngOnInit();

      expect(influenceurService.query).toHaveBeenCalled();
      expect(influenceurService.addInfluenceurToCollectionIfMissing).toHaveBeenCalledWith(influenceurCollection, influenceur);
      expect(comp.influenceursCollection()).toEqual(expectedCollection);
    });

    it('should update editForm', () => {
      const scoreReach: IScoreReach = { id: 27588 };
      const influenceur: IInfluenceur = { id: 16800 };
      scoreReach.influenceur = influenceur;

      activatedRoute.data = of({ scoreReach });
      comp.ngOnInit();

      expect(comp.influenceursCollection()).toContainEqual(influenceur);
      expect(comp.scoreReach).toEqual(scoreReach);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<IScoreReach>();
      const scoreReach = { id: 6334 };
      vitest.spyOn(scoreReachFormService, 'getScoreReach').mockReturnValue(scoreReach);
      vitest.spyOn(scoreReachService, 'update').mockReturnValue(saveSubject);
      vitest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ scoreReach });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(scoreReach);
      saveSubject.complete();

      // THEN
      expect(scoreReachFormService.getScoreReach).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(scoreReachService.update).toHaveBeenCalledWith(expect.objectContaining(scoreReach));
      expect(comp.isSaving()).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<IScoreReach>();
      const scoreReach = { id: 6334 };
      vitest.spyOn(scoreReachFormService, 'getScoreReach').mockReturnValue({ id: null });
      vitest.spyOn(scoreReachService, 'create').mockReturnValue(saveSubject);
      vitest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ scoreReach: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(scoreReach);
      saveSubject.complete();

      // THEN
      expect(scoreReachFormService.getScoreReach).toHaveBeenCalled();
      expect(scoreReachService.create).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<IScoreReach>();
      const scoreReach = { id: 6334 };
      vitest.spyOn(scoreReachService, 'update').mockReturnValue(saveSubject);
      vitest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ scoreReach });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(scoreReachService.update).toHaveBeenCalled();
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
