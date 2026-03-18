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
import { IScoreReach } from '../score-reach.model';
import { ScoreReachService } from '../service/score-reach.service';

import { ScoreReachFormGroup, ScoreReachFormService } from './score-reach-form.service';

@Component({
  selector: 'jhi-score-reach-update',
  templateUrl: './score-reach-update.html',
  imports: [FontAwesomeModule, AlertError, ReactiveFormsModule],
})
export class ScoreReachUpdate implements OnInit {
  readonly isSaving = signal(false);
  scoreReach: IScoreReach | null = null;

  influenceursCollection = signal<IInfluenceur[]>([]);

  protected scoreReachService = inject(ScoreReachService);
  protected scoreReachFormService = inject(ScoreReachFormService);
  protected influenceurService = inject(InfluenceurService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: ScoreReachFormGroup = this.scoreReachFormService.createScoreReachFormGroup();

  compareInfluenceur = (o1: IInfluenceur | null, o2: IInfluenceur | null): boolean => this.influenceurService.compareInfluenceur(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ scoreReach }) => {
      this.scoreReach = scoreReach;
      if (scoreReach) {
        this.updateForm(scoreReach);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    globalThis.history.back();
  }

  save(): void {
    this.isSaving.set(true);
    const scoreReach = this.scoreReachFormService.getScoreReach(this.editForm);
    if (scoreReach.id === null) {
      this.subscribeToSaveResponse(this.scoreReachService.create(scoreReach));
    } else {
      this.subscribeToSaveResponse(this.scoreReachService.update(scoreReach));
    }
  }

  protected subscribeToSaveResponse(result: Observable<IScoreReach | null>): void {
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

  protected updateForm(scoreReach: IScoreReach): void {
    this.scoreReach = scoreReach;
    this.scoreReachFormService.resetForm(this.editForm, scoreReach);

    this.influenceursCollection.set(
      this.influenceurService.addInfluenceurToCollectionIfMissing<IInfluenceur>(this.influenceursCollection(), scoreReach.influenceur),
    );
  }

  protected loadRelationshipsOptions(): void {
    this.influenceurService
      .query({ 'scoreReachId.specified': 'false' })
      .pipe(map((res: HttpResponse<IInfluenceur[]>) => res.body ?? []))
      .pipe(
        map((influenceurs: IInfluenceur[]) =>
          this.influenceurService.addInfluenceurToCollectionIfMissing<IInfluenceur>(influenceurs, this.scoreReach?.influenceur),
        ),
      )
      .subscribe((influenceurs: IInfluenceur[]) => this.influenceursCollection.set(influenceurs));
  }
}
