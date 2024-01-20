import { Component, Input } from '@angular/core';
import { ActivatedRoute, RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe } from 'app/shared/date';
import { INotaFiscalColeta } from '../nota-fiscal-coleta.model';

@Component({
  standalone: true,
  selector: 'jhi-nota-fiscal-coleta-detail',
  templateUrl: './nota-fiscal-coleta-detail.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class NotaFiscalColetaDetailComponent {
  @Input() notaFiscalColeta: INotaFiscalColeta | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  previousState(): void {
    window.history.back();
  }
}
