import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IHistoricoStatusColeta } from '../historico-status-coleta.model';
import { HistoricoStatusColetaService } from '../service/historico-status-coleta.service';

@Component({
  standalone: true,
  templateUrl: './historico-status-coleta-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class HistoricoStatusColetaDeleteDialogComponent {
  historicoStatusColeta?: IHistoricoStatusColeta;

  constructor(
    protected historicoStatusColetaService: HistoricoStatusColetaService,
    protected activeModal: NgbActiveModal,
  ) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.historicoStatusColetaService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
