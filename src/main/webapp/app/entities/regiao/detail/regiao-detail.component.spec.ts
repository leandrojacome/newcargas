import { TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness, RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { RegiaoDetailComponent } from './regiao-detail.component';

describe('Regiao Management Detail Component', () => {
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [RegiaoDetailComponent, RouterTestingModule.withRoutes([], { bindToComponentInputs: true })],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              component: RegiaoDetailComponent,
              resolve: { regiao: () => of({ id: 123 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(RegiaoDetailComponent, '')
      .compileComponents();
  });

  describe('OnInit', () => {
    it('Should load regiao on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', RegiaoDetailComponent);

      // THEN
      expect(instance.regiao).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
