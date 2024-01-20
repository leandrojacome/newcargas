import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IStatusColeta } from '../status-coleta.model';
import { StatusColetaService } from '../service/status-coleta.service';

@Component({
  standalone: true,
  templateUrl: './status-coleta-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class StatusColetaDeleteDialogComponent {
  statusColeta?: IStatusColeta;

  constructor(
    protected statusColetaService: StatusColetaService,
    protected activeModal: NgbActiveModal,
  ) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.statusColetaService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
