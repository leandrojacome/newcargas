import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { ITransportadora } from '../transportadora.model';
import { TransportadoraService } from '../service/transportadora.service';

@Component({
  standalone: true,
  templateUrl: './transportadora-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class TransportadoraDeleteDialogComponent {
  transportadora?: ITransportadora;

  constructor(
    protected transportadoraService: TransportadoraService,
    protected activeModal: NgbActiveModal,
  ) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.transportadoraService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
