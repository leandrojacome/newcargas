import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IBanco } from '../banco.model';
import { BancoService } from '../service/banco.service';

@Component({
  standalone: true,
  templateUrl: './banco-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class BancoDeleteDialogComponent {
  banco?: IBanco;

  constructor(
    protected bancoService: BancoService,
    protected activeModal: NgbActiveModal,
  ) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.bancoService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
