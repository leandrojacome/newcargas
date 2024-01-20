import { TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness, RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { SolicitacaoColetaDetailComponent } from './solicitacao-coleta-detail.component';

describe('SolicitacaoColeta Management Detail Component', () => {
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [SolicitacaoColetaDetailComponent, RouterTestingModule.withRoutes([], { bindToComponentInputs: true })],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              component: SolicitacaoColetaDetailComponent,
              resolve: { solicitacaoColeta: () => of({ id: 123 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(SolicitacaoColetaDetailComponent, '')
      .compileComponents();
  });

  describe('OnInit', () => {
    it('Should load solicitacaoColeta on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', SolicitacaoColetaDetailComponent);

      // THEN
      expect(instance.solicitacaoColeta).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
