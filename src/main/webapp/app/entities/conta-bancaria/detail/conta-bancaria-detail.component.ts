import { Component, Input } from '@angular/core';
import { ActivatedRoute, RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe } from 'app/shared/date';
import { IContaBancaria } from '../conta-bancaria.model';

@Component({
  standalone: true,
  selector: 'jhi-conta-bancaria-detail',
  templateUrl: './conta-bancaria-detail.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class ContaBancariaDetailComponent {
  @Input() contaBancaria: IContaBancaria | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  previousState(): void {
    window.history.back();
  }
}
