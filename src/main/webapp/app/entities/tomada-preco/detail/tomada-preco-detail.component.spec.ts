import { TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness, RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { TomadaPrecoDetailComponent } from './tomada-preco-detail.component';

describe('TomadaPreco Management Detail Component', () => {
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [TomadaPrecoDetailComponent, RouterTestingModule.withRoutes([], { bindToComponentInputs: true })],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              component: TomadaPrecoDetailComponent,
              resolve: { tomadaPreco: () => of({ id: 123 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(TomadaPrecoDetailComponent, '')
      .compileComponents();
  });

  describe('OnInit', () => {
    it('Should load tomadaPreco on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', TomadaPrecoDetailComponent);

      // THEN
      expect(instance.tomadaPreco).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
