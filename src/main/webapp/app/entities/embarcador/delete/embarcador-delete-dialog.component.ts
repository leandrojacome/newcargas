import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IEmbarcador } from '../embarcador.model';
import { EmbarcadorService } from '../service/embarcador.service';

@Component({
  standalone: true,
  templateUrl: './embarcador-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class EmbarcadorDeleteDialogComponent {
  embarcador?: IEmbarcador;

  constructor(
    protected embarcadorService: EmbarcadorService,
    protected activeModal: NgbActiveModal,
  ) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.embarcadorService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
