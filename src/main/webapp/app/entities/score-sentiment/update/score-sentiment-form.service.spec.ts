import { beforeEach, describe, expect, it } from 'vitest';
import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../score-sentiment.test-samples';

import { ScoreSentimentFormService } from './score-sentiment-form.service';

describe('ScoreSentiment Form Service', () => {
  let service: ScoreSentimentFormService;

  beforeEach(() => {
    service = TestBed.inject(ScoreSentimentFormService);
  });

  describe('Service methods', () => {
    describe('createScoreSentimentFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createScoreSentimentFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            ratioPositif: expect.any(Object),
            ratioNegatif: expect.any(Object),
            ratioNeutre: expect.any(Object),
            scoreSentiment: expect.any(Object),
            influenceur: expect.any(Object),
          }),
        );
      });

      it('passing IScoreSentiment should create a new form with FormGroup', () => {
        const formGroup = service.createScoreSentimentFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            ratioPositif: expect.any(Object),
            ratioNegatif: expect.any(Object),
            ratioNeutre: expect.any(Object),
            scoreSentiment: expect.any(Object),
            influenceur: expect.any(Object),
          }),
        );
      });
    });

    describe('getScoreSentiment', () => {
      it('should return NewScoreSentiment for default ScoreSentiment initial value', () => {
        const formGroup = service.createScoreSentimentFormGroup(sampleWithNewData);

        const scoreSentiment = service.getScoreSentiment(formGroup);

        expect(scoreSentiment).toMatchObject(sampleWithNewData);
      });

      it('should return NewScoreSentiment for empty ScoreSentiment initial value', () => {
        const formGroup = service.createScoreSentimentFormGroup();

        const scoreSentiment = service.getScoreSentiment(formGroup);

        expect(scoreSentiment).toMatchObject({});
      });

      it('should return IScoreSentiment', () => {
        const formGroup = service.createScoreSentimentFormGroup(sampleWithRequiredData);

        const scoreSentiment = service.getScoreSentiment(formGroup);

        expect(scoreSentiment).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IScoreSentiment should not enable id FormControl', () => {
        const formGroup = service.createScoreSentimentFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewScoreSentiment should disable id FormControl', () => {
        const formGroup = service.createScoreSentimentFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
