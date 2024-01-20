import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IFatura } from '../fatura.model';
import { FaturaService } from '../service/fatura.service';

@Component({
  standalone: true,
  templateUrl: './fatura-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class FaturaDeleteDialogComponent {
  fatura?: IFatura;

  constructor(
    protected faturaService: FaturaService,
    protected activeModal: NgbActiveModal,
  ) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.faturaService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
