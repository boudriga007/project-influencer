import { HttpResponse } from '@angular/common/http';
import { Component, OnInit, inject, signal } from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IInfluenceur } from 'app/entities/influenceur/influenceur.model';
import { InfluenceurService } from 'app/entities/influenceur/service/influenceur.service';
import { AlertError } from 'app/shared/alert/alert-error';
import { IScoreSentiment } from '../score-sentiment.model';
import { ScoreSentimentService } from '../service/score-sentiment.service';

import { ScoreSentimentFormGroup, ScoreSentimentFormService } from './score-sentiment-form.service';

@Component({
  selector: 'jhi-score-sentiment-update',
  templateUrl: './score-sentiment-update.html',
  imports: [FontAwesomeModule, AlertError, ReactiveFormsModule],
})
export class ScoreSentimentUpdate implements OnInit {
  readonly isSaving = signal(false);
  scoreSentiment: IScoreSentiment | null = null;

  influenceursCollection = signal<IInfluenceur[]>([]);

  protected scoreSentimentService = inject(ScoreSentimentService);
  protected scoreSentimentFormService = inject(ScoreSentimentFormService);
  protected influenceurService = inject(InfluenceurService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: ScoreSentimentFormGroup = this.scoreSentimentFormService.createScoreSentimentFormGroup();

  compareInfluenceur = (o1: IInfluenceur | null, o2: IInfluenceur | null): boolean => this.influenceurService.compareInfluenceur(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ scoreSentiment }) => {
      this.scoreSentiment = scoreSentiment;
      if (scoreSentiment) {
        this.updateForm(scoreSentiment);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    globalThis.history.back();
  }

  save(): void {
    this.isSaving.set(true);
    const scoreSentiment = this.scoreSentimentFormService.getScoreSentiment(this.editForm);
    if (scoreSentiment.id === null) {
      this.subscribeToSaveResponse(this.scoreSentimentService.create(scoreSentiment));
    } else {
      this.subscribeToSaveResponse(this.scoreSentimentService.update(scoreSentiment));
    }
  }

  protected subscribeToSaveResponse(result: Observable<IScoreSentiment | null>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving.set(false);
  }

  protected updateForm(scoreSentiment: IScoreSentiment): void {
    this.scoreSentiment = scoreSentiment;
    this.scoreSentimentFormService.resetForm(this.editForm, scoreSentiment);

    this.influenceursCollection.set(
      this.influenceurService.addInfluenceurToCollectionIfMissing<IInfluenceur>(this.influenceursCollection(), scoreSentiment.influenceur),
    );
  }

  protected loadRelationshipsOptions(): void {
    this.influenceurService
      .query({ 'scoreSentimentId.specified': 'false' })
      .pipe(map((res: HttpResponse<IInfluenceur[]>) => res.body ?? []))
      .pipe(
        map((influenceurs: IInfluenceur[]) =>
          this.influenceurService.addInfluenceurToCollectionIfMissing<IInfluenceur>(influenceurs, this.scoreSentiment?.influenceur),
        ),
      )
      .subscribe((influenceurs: IInfluenceur[]) => this.influenceursCollection.set(influenceurs));
  }
}
