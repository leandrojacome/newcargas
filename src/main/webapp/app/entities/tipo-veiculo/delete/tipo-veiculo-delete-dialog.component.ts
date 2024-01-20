import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { ITipoVeiculo } from '../tipo-veiculo.model';
import { TipoVeiculoService } from '../service/tipo-veiculo.service';

@Component({
  standalone: true,
  templateUrl: './tipo-veiculo-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class TipoVeiculoDeleteDialogComponent {
  tipoVeiculo?: ITipoVeiculo;

  constructor(
    protected tipoVeiculoService: TipoVeiculoService,
    protected activeModal: NgbActiveModal,
  ) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.tipoVeiculoService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
