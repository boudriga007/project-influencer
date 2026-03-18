import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap/modal';

import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { AlertError } from 'app/shared/alert/alert-error';
import { IInfluenceur } from '../influenceur.model';
import { InfluenceurService } from '../service/influenceur.service';

@Component({
  templateUrl: './influenceur-delete-dialog.html',
  imports: [FormsModule, FontAwesomeModule, AlertError],
})
export class InfluenceurDeleteDialog {
  influenceur?: IInfluenceur;

  protected readonly influenceurService = inject(InfluenceurService);
  protected readonly activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.influenceurService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
