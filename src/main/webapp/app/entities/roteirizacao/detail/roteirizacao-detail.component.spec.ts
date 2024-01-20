import { TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness, RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { RoteirizacaoDetailComponent } from './roteirizacao-detail.component';

describe('Roteirizacao Management Detail Component', () => {
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [RoteirizacaoDetailComponent, RouterTestingModule.withRoutes([], { bindToComponentInputs: true })],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              component: RoteirizacaoDetailComponent,
              resolve: { roteirizacao: () => of({ id: 123 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(RoteirizacaoDetailComponent, '')
      .compileComponents();
  });

  describe('OnInit', () => {
    it('Should load roteirizacao on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', RoteirizacaoDetailComponent);

      // THEN
      expect(instance.roteirizacao).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
