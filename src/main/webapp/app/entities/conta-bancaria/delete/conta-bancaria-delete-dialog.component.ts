import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IContaBancaria } from '../conta-bancaria.model';
import { ContaBancariaService } from '../service/conta-bancaria.service';

@Component({
  standalone: true,
  templateUrl: './conta-bancaria-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class ContaBancariaDeleteDialogComponent {
  contaBancaria?: IContaBancaria;

  constructor(
    protected contaBancariaService: ContaBancariaService,
    protected activeModal: NgbActiveModal,
  ) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.contaBancariaService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
