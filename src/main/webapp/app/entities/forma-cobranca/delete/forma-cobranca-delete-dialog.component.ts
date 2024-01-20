import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IFormaCobranca } from '../forma-cobranca.model';
import { FormaCobrancaService } from '../service/forma-cobranca.service';

@Component({
  standalone: true,
  templateUrl: './forma-cobranca-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class FormaCobrancaDeleteDialogComponent {
  formaCobranca?: IFormaCobranca;

  constructor(
    protected formaCobrancaService: FormaCobrancaService,
    protected activeModal: NgbActiveModal,
  ) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.formaCobrancaService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
