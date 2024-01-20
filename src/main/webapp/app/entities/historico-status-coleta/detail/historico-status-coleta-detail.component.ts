import { Component, Input } from '@angular/core';
import { ActivatedRoute, RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe } from 'app/shared/date';
import { IHistoricoStatusColeta } from '../historico-status-coleta.model';

@Component({
  standalone: true,
  selector: 'jhi-historico-status-coleta-detail',
  templateUrl: './historico-status-coleta-detail.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class HistoricoStatusColetaDetailComponent {
  @Input() historicoStatusColeta: IHistoricoStatusColeta | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  previousState(): void {
    window.history.back();
  }
}
