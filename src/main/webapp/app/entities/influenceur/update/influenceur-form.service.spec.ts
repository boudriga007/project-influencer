import { beforeEach, describe, expect, it } from 'vitest';
import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../influenceur.test-samples';

import { InfluenceurFormService } from './influenceur-form.service';

describe('Influenceur Form Service', () => {
  let service: InfluenceurFormService;

  beforeEach(() => {
    service = TestBed.inject(InfluenceurFormService);
  });

  describe('Service methods', () => {
    describe('createInfluenceurFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createInfluenceurFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            nom: expect.any(Object),
            username: expect.any(Object),
            photoUrl: expect.any(Object),
            bio: expect.any(Object),
            plateforme: expect.any(Object),
            categorie: expect.any(Object),
            scoreGlobal: expect.any(Object),
          }),
        );
      });

      it('passing IInfluenceur should create a new form with FormGroup', () => {
        const formGroup = service.createInfluenceurFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            nom: expect.any(Object),
            username: expect.any(Object),
            photoUrl: expect.any(Object),
            bio: expect.any(Object),
            plateforme: expect.any(Object),
            categorie: expect.any(Object),
            scoreGlobal: expect.any(Object),
          }),
        );
      });
    });

    describe('getInfluenceur', () => {
      it('should return NewInfluenceur for default Influenceur initial value', () => {
        const formGroup = service.createInfluenceurFormGroup(sampleWithNewData);

        const influenceur = service.getInfluenceur(formGroup);

        expect(influenceur).toMatchObject(sampleWithNewData);
      });

      it('should return NewInfluenceur for empty Influenceur initial value', () => {
        const formGroup = service.createInfluenceurFormGroup();

        const influenceur = service.getInfluenceur(formGroup);

        expect(influenceur).toMatchObject({});
      });

      it('should return IInfluenceur', () => {
        const formGroup = service.createInfluenceurFormGroup(sampleWithRequiredData);

        const influenceur = service.getInfluenceur(formGroup);

        expect(influenceur).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IInfluenceur should not enable id FormControl', () => {
        const formGroup = service.createInfluenceurFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewInfluenceur should disable id FormControl', () => {
        const formGroup = service.createInfluenceurFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
