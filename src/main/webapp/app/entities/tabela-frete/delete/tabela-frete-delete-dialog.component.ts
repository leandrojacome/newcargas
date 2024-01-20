import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { ITabelaFrete } from '../tabela-frete.model';
import { TabelaFreteService } from '../service/tabela-frete.service';

@Component({
  standalone: true,
  templateUrl: './tabela-frete-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class TabelaFreteDeleteDialogComponent {
  tabelaFrete?: ITabelaFrete;

  constructor(
    protected tabelaFreteService: TabelaFreteService,
    protected activeModal: NgbActiveModal,
  ) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.tabelaFreteService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
