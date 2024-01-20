import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { ISolicitacaoColeta } from '../solicitacao-coleta.model';
import { SolicitacaoColetaService } from '../service/solicitacao-coleta.service';

@Component({
  standalone: true,
  templateUrl: './solicitacao-coleta-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class SolicitacaoColetaDeleteDialogComponent {
  solicitacaoColeta?: ISolicitacaoColeta;

  constructor(
    protected solicitacaoColetaService: SolicitacaoColetaService,
    protected activeModal: NgbActiveModal,
  ) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.solicitacaoColetaService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
