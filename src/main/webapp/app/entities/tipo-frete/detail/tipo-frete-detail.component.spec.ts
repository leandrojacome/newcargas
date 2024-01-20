import { TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness, RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { TipoFreteDetailComponent } from './tipo-frete-detail.component';

describe('TipoFrete Management Detail Component', () => {
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [TipoFreteDetailComponent, RouterTestingModule.withRoutes([], { bindToComponentInputs: true })],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              component: TipoFreteDetailComponent,
              resolve: { tipoFrete: () => of({ id: 123 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(TipoFreteDetailComponent, '')
      .compileComponents();
  });

  describe('OnInit', () => {
    it('Should load tipoFrete on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', TipoFreteDetailComponent);

      // THEN
      expect(instance.tipoFrete).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
