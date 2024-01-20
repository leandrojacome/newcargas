import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IContratacao } from '../contratacao.model';
import { ContratacaoService } from '../service/contratacao.service';

@Component({
  standalone: true,
  templateUrl: './contratacao-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class ContratacaoDeleteDialogComponent {
  contratacao?: IContratacao;

  constructor(
    protected contratacaoService: ContratacaoService,
    protected activeModal: NgbActiveModal,
  ) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.contratacaoService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
