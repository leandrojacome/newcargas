import { TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness, RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { TipoVeiculoDetailComponent } from './tipo-veiculo-detail.component';

describe('TipoVeiculo Management Detail Component', () => {
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [TipoVeiculoDetailComponent, RouterTestingModule.withRoutes([], { bindToComponentInputs: true })],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              component: TipoVeiculoDetailComponent,
              resolve: { tipoVeiculo: () => of({ id: 123 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(TipoVeiculoDetailComponent, '')
      .compileComponents();
  });

  describe('OnInit', () => {
    it('Should load tipoVeiculo on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', TipoVeiculoDetailComponent);

      // THEN
      expect(instance.tipoVeiculo).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
