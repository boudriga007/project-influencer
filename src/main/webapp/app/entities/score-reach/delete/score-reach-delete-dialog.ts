import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap/modal';

import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { AlertError } from 'app/shared/alert/alert-error';
import { IScoreReach } from '../score-reach.model';
import { ScoreReachService } from '../service/score-reach.service';

@Component({
  templateUrl: './score-reach-delete-dialog.html',
  imports: [FormsModule, FontAwesomeModule, AlertError],
})
export class ScoreReachDeleteDialog {
  scoreReach?: IScoreReach;

  protected readonly scoreReachService = inject(ScoreReachService);
  protected readonly activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.scoreReachService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
