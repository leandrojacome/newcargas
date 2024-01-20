import { TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness, RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { FaturaDetailComponent } from './fatura-detail.component';

describe('Fatura Management Detail Component', () => {
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [FaturaDetailComponent, RouterTestingModule.withRoutes([], { bindToComponentInputs: true })],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              component: FaturaDetailComponent,
              resolve: { fatura: () => of({ id: 123 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(FaturaDetailComponent, '')
      .compileComponents();
  });

  describe('OnInit', () => {
    it('Should load fatura on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', FaturaDetailComponent);

      // THEN
      expect(instance.fatura).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
