import { Component, input } from '@angular/core';
import { RouterLink } from '@angular/router';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';

import { Alert } from 'app/shared/alert/alert';
import { AlertError } from 'app/shared/alert/alert-error';
import { IScoreReach } from '../score-reach.model';

@Component({
  selector: 'jhi-score-reach-detail',
  templateUrl: './score-reach-detail.html',
  imports: [FontAwesomeModule, Alert, AlertError, RouterLink],
})
export class ScoreReachDetail {
  readonly scoreReach = input<IScoreReach | null>(null);

  previousState(): void {
    globalThis.history.back();
  }
}
