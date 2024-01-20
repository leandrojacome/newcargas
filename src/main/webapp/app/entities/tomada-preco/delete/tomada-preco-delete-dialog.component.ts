import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { ITomadaPreco } from '../tomada-preco.model';
import { TomadaPrecoService } from '../service/tomada-preco.service';

@Component({
  standalone: true,
  templateUrl: './tomada-preco-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class TomadaPrecoDeleteDialogComponent {
  tomadaPreco?: ITomadaPreco;

  constructor(
    protected tomadaPrecoService: TomadaPrecoService,
    protected activeModal: NgbActiveModal,
  ) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.tomadaPrecoService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
