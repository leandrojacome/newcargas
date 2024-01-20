import { TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness, RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { TabelaFreteDetailComponent } from './tabela-frete-detail.component';

describe('TabelaFrete Management Detail Component', () => {
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [TabelaFreteDetailComponent, RouterTestingModule.withRoutes([], { bindToComponentInputs: true })],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              component: TabelaFreteDetailComponent,
              resolve: { tabelaFrete: () => of({ id: 123 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(TabelaFreteDetailComponent, '')
      .compileComponents();
  });

  describe('OnInit', () => {
    it('Should load tabelaFrete on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', TabelaFreteDetailComponent);

      // THEN
      expect(instance.tabelaFrete).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
