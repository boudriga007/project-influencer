import { Component, OnInit, inject, signal } from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { Categorie } from 'app/entities/enumerations/categorie.model';
import { Plateforme } from 'app/entities/enumerations/plateforme.model';
import { AlertError } from 'app/shared/alert/alert-error';
import { IInfluenceur } from '../influenceur.model';
import { InfluenceurService } from '../service/influenceur.service';

import { InfluenceurFormGroup, InfluenceurFormService } from './influenceur-form.service';

@Component({
  selector: 'jhi-influenceur-update',
  templateUrl: './influenceur-update.html',
  imports: [FontAwesomeModule, AlertError, ReactiveFormsModule],
})
export class InfluenceurUpdate implements OnInit {
  readonly isSaving = signal(false);
  influenceur: IInfluenceur | null = null;
  plateformeValues = Object.keys(Plateforme);
  categorieValues = Object.keys(Categorie);

  protected influenceurService = inject(InfluenceurService);
  protected influenceurFormService = inject(InfluenceurFormService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: InfluenceurFormGroup = this.influenceurFormService.createInfluenceurFormGroup();

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ influenceur }) => {
      this.influenceur = influenceur;
      if (influenceur) {
        this.updateForm(influenceur);
      }
    });
  }

  previousState(): void {
    globalThis.history.back();
  }

  save(): void {
    this.isSaving.set(true);
    const influenceur = this.influenceurFormService.getInfluenceur(this.editForm);
    if (influenceur.id === null) {
      this.subscribeToSaveResponse(this.influenceurService.create(influenceur));
    } else {
      this.subscribeToSaveResponse(this.influenceurService.update(influenceur));
    }
  }

  protected subscribeToSaveResponse(result: Observable<IInfluenceur | null>): void {
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

  protected updateForm(influenceur: IInfluenceur): void {
    this.influenceur = influenceur;
    this.influenceurFormService.resetForm(this.editForm, influenceur);
  }
}
