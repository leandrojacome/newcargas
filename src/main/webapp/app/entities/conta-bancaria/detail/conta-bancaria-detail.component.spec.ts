import { TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness, RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { ContaBancariaDetailComponent } from './conta-bancaria-detail.component';

describe('ContaBancaria Management Detail Component', () => {
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ContaBancariaDetailComponent, RouterTestingModule.withRoutes([], { bindToComponentInputs: true })],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              component: ContaBancariaDetailComponent,
              resolve: { contaBancaria: () => of({ id: 123 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(ContaBancariaDetailComponent, '')
      .compileComponents();
  });

  describe('OnInit', () => {
    it('Should load contaBancaria on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', ContaBancariaDetailComponent);

      // THEN
      expect(instance.contaBancaria).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
