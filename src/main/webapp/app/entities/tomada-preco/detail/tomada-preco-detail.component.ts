import { Component, Input } from '@angular/core';
import { ActivatedRoute, RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe } from 'app/shared/date';
import { ITomadaPreco } from '../tomada-preco.model';

@Component({
  standalone: true,
  selector: 'jhi-tomada-preco-detail',
  templateUrl: './tomada-preco-detail.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class TomadaPrecoDetailComponent {
  @Input() tomadaPreco: ITomadaPreco | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  previousState(): void {
    window.history.back();
  }
}
