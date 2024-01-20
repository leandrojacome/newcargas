import { TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness, RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { NotaFiscalColetaDetailComponent } from './nota-fiscal-coleta-detail.component';

describe('NotaFiscalColeta Management Detail Component', () => {
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [NotaFiscalColetaDetailComponent, RouterTestingModule.withRoutes([], { bindToComponentInputs: true })],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              component: NotaFiscalColetaDetailComponent,
              resolve: { notaFiscalColeta: () => of({ id: 123 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(NotaFiscalColetaDetailComponent, '')
      .compileComponents();
  });

  describe('OnInit', () => {
    it('Should load notaFiscalColeta on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', NotaFiscalColetaDetailComponent);

      // THEN
      expect(instance.notaFiscalColeta).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
