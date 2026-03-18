import { beforeEach, describe, expect, it, vitest } from 'vitest';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';

import { FaIconLibrary } from '@fortawesome/angular-fontawesome';
import { faArrowLeft, faPencilAlt } from '@fortawesome/free-solid-svg-icons';
import { of } from 'rxjs';

import { ScoreSentimentDetail } from './score-sentiment-detail';

describe('ScoreSentiment Management Detail Component', () => {
  let comp: ScoreSentimentDetail;
  let fixture: ComponentFixture<ScoreSentimentDetail>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [
        provideRouter(
          [
            {
              path: '**',
              loadComponent: () => import('./score-sentiment-detail').then(m => m.ScoreSentimentDetail),
              resolve: { scoreSentiment: () => of({ id: 17421 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    });
    const library = TestBed.inject(FaIconLibrary);
    library.addIcons(faArrowLeft);
    library.addIcons(faPencilAlt);
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ScoreSentimentDetail);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('should load scoreSentiment on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', ScoreSentimentDetail);

      // THEN
      expect(instance.scoreSentiment()).toEqual(expect.objectContaining({ id: 17421 }));
    });
  });

  describe('PreviousState', () => {
    it('should navigate to previous state', () => {
      vitest.spyOn(globalThis.history, 'back');
      comp.previousState();
      expect(globalThis.history.back).toHaveBeenCalled();
    });
  });
});
