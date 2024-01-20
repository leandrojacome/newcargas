import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { INotaFiscalColeta } from '../nota-fiscal-coleta.model';
import { NotaFiscalColetaService } from '../service/nota-fiscal-coleta.service';

@Component({
  standalone: true,
  templateUrl: './nota-fiscal-coleta-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class NotaFiscalColetaDeleteDialogComponent {
  notaFiscalColeta?: INotaFiscalColeta;

  constructor(
    protected notaFiscalColetaService: NotaFiscalColetaService,
    protected activeModal: NgbActiveModal,
  ) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.notaFiscalColetaService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
