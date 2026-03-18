import { Component, input } from '@angular/core';
import { RouterLink } from '@angular/router';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';

import { Alert } from 'app/shared/alert/alert';
import { AlertError } from 'app/shared/alert/alert-error';
import { IScoreSentiment } from '../score-sentiment.model';

@Component({
  selector: 'jhi-score-sentiment-detail',
  templateUrl: './score-sentiment-detail.html',
  imports: [FontAwesomeModule, Alert, AlertError, RouterLink],
})
export class ScoreSentimentDetail {
  readonly scoreSentiment = input<IScoreSentiment | null>(null);

  previousState(): void {
    globalThis.history.back();
  }
}
