import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { ITipoFrete } from '../tipo-frete.model';
import { TipoFreteService } from '../service/tipo-frete.service';

@Component({
  standalone: true,
  templateUrl: './tipo-frete-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class TipoFreteDeleteDialogComponent {
  tipoFrete?: ITipoFrete;

  constructor(
    protected tipoFreteService: TipoFreteService,
    protected activeModal: NgbActiveModal,
  ) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.tipoFreteService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
