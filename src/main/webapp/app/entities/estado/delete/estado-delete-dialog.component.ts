import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IEstado } from '../estado.model';
import { EstadoService } from '../service/estado.service';

@Component({
  standalone: true,
  templateUrl: './estado-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class EstadoDeleteDialogComponent {
  estado?: IEstado;

  constructor(
    protected estadoService: EstadoService,
    protected activeModal: NgbActiveModal,
  ) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.estadoService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
