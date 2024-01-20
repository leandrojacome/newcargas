import { Component, Input } from '@angular/core';
import { ActivatedRoute, RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe } from 'app/shared/date';
import { IRoteirizacao } from '../roteirizacao.model';

@Component({
  standalone: true,
  selector: 'jhi-roteirizacao-detail',
  templateUrl: './roteirizacao-detail.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class RoteirizacaoDetailComponent {
  @Input() roteirizacao: IRoteirizacao | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  previousState(): void {
    window.history.back();
  }
}
