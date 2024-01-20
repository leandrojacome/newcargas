import { Component, Input } from '@angular/core';
import { ActivatedRoute, RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe } from 'app/shared/date';
import { ISolicitacaoColeta } from '../solicitacao-coleta.model';

@Component({
  standalone: true,
  selector: 'jhi-solicitacao-coleta-detail',
  templateUrl: './solicitacao-coleta-detail.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class SolicitacaoColetaDetailComponent {
  @Input() solicitacaoColeta: ISolicitacaoColeta | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  previousState(): void {
    window.history.back();
  }
}
