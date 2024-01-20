import { TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness, RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { HistoricoStatusColetaDetailComponent } from './historico-status-coleta-detail.component';

describe('HistoricoStatusColeta Management Detail Component', () => {
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [HistoricoStatusColetaDetailComponent, RouterTestingModule.withRoutes([], { bindToComponentInputs: true })],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              component: HistoricoStatusColetaDetailComponent,
              resolve: { historicoStatusColeta: () => of({ id: 123 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(HistoricoStatusColetaDetailComponent, '')
      .compileComponents();
  });

  describe('OnInit', () => {
    it('Should load historicoStatusColeta on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', HistoricoStatusColetaDetailComponent);

      // THEN
      expect(instance.historicoStatusColeta).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
