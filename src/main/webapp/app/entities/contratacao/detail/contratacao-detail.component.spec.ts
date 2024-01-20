import { TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness, RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { ContratacaoDetailComponent } from './contratacao-detail.component';

describe('Contratacao Management Detail Component', () => {
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ContratacaoDetailComponent, RouterTestingModule.withRoutes([], { bindToComponentInputs: true })],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              component: ContratacaoDetailComponent,
              resolve: { contratacao: () => of({ id: 123 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(ContratacaoDetailComponent, '')
      .compileComponents();
  });

  describe('OnInit', () => {
    it('Should load contratacao on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', ContratacaoDetailComponent);

      // THEN
      expect(instance.contratacao).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
