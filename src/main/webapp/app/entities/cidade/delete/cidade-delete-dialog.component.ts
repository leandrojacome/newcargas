import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { ICidade } from '../cidade.model';
import { CidadeService } from '../service/cidade.service';

@Component({
  standalone: true,
  templateUrl: './cidade-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class CidadeDeleteDialogComponent {
  cidade?: ICidade;

  constructor(
    protected cidadeService: CidadeService,
    protected activeModal: NgbActiveModal,
  ) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.cidadeService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
