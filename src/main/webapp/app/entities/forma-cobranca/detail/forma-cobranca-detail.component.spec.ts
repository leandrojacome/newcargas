import { TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness, RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { FormaCobrancaDetailComponent } from './forma-cobranca-detail.component';

describe('FormaCobranca Management Detail Component', () => {
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [FormaCobrancaDetailComponent, RouterTestingModule.withRoutes([], { bindToComponentInputs: true })],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              component: FormaCobrancaDetailComponent,
              resolve: { formaCobranca: () => of({ id: 123 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(FormaCobrancaDetailComponent, '')
      .compileComponents();
  });

  describe('OnInit', () => {
    it('Should load formaCobranca on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', FormaCobrancaDetailComponent);

      // THEN
      expect(instance.formaCobranca).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
