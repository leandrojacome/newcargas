import { Component, Input } from '@angular/core';
import { ActivatedRoute, RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe } from 'app/shared/date';
import { IFormaCobranca } from '../forma-cobranca.model';

@Component({
  standalone: true,
  selector: 'jhi-forma-cobranca-detail',
  templateUrl: './forma-cobranca-detail.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class FormaCobrancaDetailComponent {
  @Input() formaCobranca: IFormaCobranca | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  previousState(): void {
    window.history.back();
  }
}
