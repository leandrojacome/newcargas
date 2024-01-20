import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { INotificacao } from '../notificacao.model';
import { NotificacaoService } from '../service/notificacao.service';

@Component({
  standalone: true,
  templateUrl: './notificacao-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class NotificacaoDeleteDialogComponent {
  notificacao?: INotificacao;

  constructor(
    protected notificacaoService: NotificacaoService,
    protected activeModal: NgbActiveModal,
  ) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.notificacaoService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
