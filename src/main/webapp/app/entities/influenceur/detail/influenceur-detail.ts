import { Component, input } from '@angular/core';
import { RouterLink } from '@angular/router';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';

import { Alert } from 'app/shared/alert/alert';
import { AlertError } from 'app/shared/alert/alert-error';
import { IInfluenceur } from '../influenceur.model';

@Component({
  selector: 'jhi-influenceur-detail',
  templateUrl: './influenceur-detail.html',
  imports: [FontAwesomeModule, Alert, AlertError, RouterLink],
})
export class InfluenceurDetail {
  readonly influenceur = input<IInfluenceur | null>(null);

  previousState(): void {
    globalThis.history.back();
  }
}
