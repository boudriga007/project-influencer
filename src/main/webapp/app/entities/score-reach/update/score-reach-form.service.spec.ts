import { beforeEach, describe, expect, it } from 'vitest';
import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../score-reach.test-samples';

import { ScoreReachFormService } from './score-reach-form.service';

describe('ScoreReach Form Service', () => {
  let service: ScoreReachFormService;

  beforeEach(() => {
    service = TestBed.inject(ScoreReachFormService);
  });

  describe('Service methods', () => {
    describe('createScoreReachFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createScoreReachFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            nbFollowers: expect.any(Object),
            engagementRate: expect.any(Object),
            reachEstime: expect.any(Object),
            scoreReach: expect.any(Object),
            influenceur: expect.any(Object),
          }),
        );
      });

      it('passing IScoreReach should create a new form with FormGroup', () => {
        const formGroup = service.createScoreReachFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            nbFollowers: expect.any(Object),
            engagementRate: expect.any(Object),
            reachEstime: expect.any(Object),
            scoreReach: expect.any(Object),
            influenceur: expect.any(Object),
          }),
        );
      });
    });

    describe('getScoreReach', () => {
      it('should return NewScoreReach for default ScoreReach initial value', () => {
        const formGroup = service.createScoreReachFormGroup(sampleWithNewData);

        const scoreReach = service.getScoreReach(formGroup);

        expect(scoreReach).toMatchObject(sampleWithNewData);
      });

      it('should return NewScoreReach for empty ScoreReach initial value', () => {
        const formGroup = service.createScoreReachFormGroup();

        const scoreReach = service.getScoreReach(formGroup);

        expect(scoreReach).toMatchObject({});
      });

      it('should return IScoreReach', () => {
        const formGroup = service.createScoreReachFormGroup(sampleWithRequiredData);

        const scoreReach = service.getScoreReach(formGroup);

        expect(scoreReach).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IScoreReach should not enable id FormControl', () => {
        const formGroup = service.createScoreReachFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewScoreReach should disable id FormControl', () => {
        const formGroup = service.createScoreReachFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
