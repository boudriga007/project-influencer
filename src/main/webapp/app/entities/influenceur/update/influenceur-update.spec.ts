import { beforeEach, describe, expect, it, vitest } from 'vitest';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';

import { Subject, from, of } from 'rxjs';

import { IInfluenceur } from '../influenceur.model';
import { InfluenceurService } from '../service/influenceur.service';

import { InfluenceurFormService } from './influenceur-form.service';
import { InfluenceurUpdate } from './influenceur-update';

describe('Influenceur Management Update Component', () => {
  let comp: InfluenceurUpdate;
  let fixture: ComponentFixture<InfluenceurUpdate>;
  let activatedRoute: ActivatedRoute;
  let influenceurFormService: InfluenceurFormService;
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

    fixture = TestBed.createComponent(InfluenceurUpdate);
    activatedRoute = TestBed.inject(ActivatedRoute);
    influenceurFormService = TestBed.inject(InfluenceurFormService);
    influenceurService = TestBed.inject(InfluenceurService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should update editForm', () => {
      const influenceur: IInfluenceur = { id: 28452 };

      activatedRoute.data = of({ influenceur });
      comp.ngOnInit();

      expect(comp.influenceur).toEqual(influenceur);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<IInfluenceur>();
      const influenceur = { id: 16800 };
      vitest.spyOn(influenceurFormService, 'getInfluenceur').mockReturnValue(influenceur);
      vitest.spyOn(influenceurService, 'update').mockReturnValue(saveSubject);
      vitest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ influenceur });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(influenceur);
      saveSubject.complete();

      // THEN
      expect(influenceurFormService.getInfluenceur).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(influenceurService.update).toHaveBeenCalledWith(expect.objectContaining(influenceur));
      expect(comp.isSaving()).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<IInfluenceur>();
      const influenceur = { id: 16800 };
      vitest.spyOn(influenceurFormService, 'getInfluenceur').mockReturnValue({ id: null });
      vitest.spyOn(influenceurService, 'create').mockReturnValue(saveSubject);
      vitest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ influenceur: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(influenceur);
      saveSubject.complete();

      // THEN
      expect(influenceurFormService.getInfluenceur).toHaveBeenCalled();
      expect(influenceurService.create).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<IInfluenceur>();
      const influenceur = { id: 16800 };
      vitest.spyOn(influenceurService, 'update').mockReturnValue(saveSubject);
      vitest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ influenceur });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(influenceurService.update).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
