import { TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness, RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { StatusColetaDetailComponent } from './status-coleta-detail.component';

describe('StatusColeta Management Detail Component', () => {
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [StatusColetaDetailComponent, RouterTestingModule.withRoutes([], { bindToComponentInputs: true })],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              component: StatusColetaDetailComponent,
              resolve: { statusColeta: () => of({ id: 123 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(StatusColetaDetailComponent, '')
      .compileComponents();
  });

  describe('OnInit', () => {
    it('Should load statusColeta on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', StatusColetaDetailComponent);

      // THEN
      expect(instance.statusColeta).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
