import { Component, Input } from '@angular/core';
import { ActivatedRoute, RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe } from 'app/shared/date';
import { ITipoVeiculo } from '../tipo-veiculo.model';

@Component({
  standalone: true,
  selector: 'jhi-tipo-veiculo-detail',
  templateUrl: './tipo-veiculo-detail.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class TipoVeiculoDetailComponent {
  @Input() tipoVeiculo: ITipoVeiculo | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  previousState(): void {
    window.history.back();
  }
}
