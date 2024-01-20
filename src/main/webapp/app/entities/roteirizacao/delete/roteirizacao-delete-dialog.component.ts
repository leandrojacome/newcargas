import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IRoteirizacao } from '../roteirizacao.model';
import { RoteirizacaoService } from '../service/roteirizacao.service';

@Component({
  standalone: true,
  templateUrl: './roteirizacao-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class RoteirizacaoDeleteDialogComponent {
  roteirizacao?: IRoteirizacao;

  constructor(
    protected roteirizacaoService: RoteirizacaoService,
    protected activeModal: NgbActiveModal,
  ) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.roteirizacaoService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
