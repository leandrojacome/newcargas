import { TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness, RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { TipoCargaDetailComponent } from './tipo-carga-detail.component';

describe('TipoCarga Management Detail Component', () => {
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [TipoCargaDetailComponent, RouterTestingModule.withRoutes([], { bindToComponentInputs: true })],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              component: TipoCargaDetailComponent,
              resolve: { tipoCarga: () => of({ id: 123 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(TipoCargaDetailComponent, '')
      .compileComponents();
  });

  describe('OnInit', () => {
    it('Should load tipoCarga on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', TipoCargaDetailComponent);

      // THEN
      expect(instance.tipoCarga).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
