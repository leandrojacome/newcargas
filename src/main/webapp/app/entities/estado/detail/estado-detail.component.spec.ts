import { TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness, RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { EstadoDetailComponent } from './estado-detail.component';

describe('Estado Management Detail Component', () => {
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [EstadoDetailComponent, RouterTestingModule.withRoutes([], { bindToComponentInputs: true })],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              component: EstadoDetailComponent,
              resolve: { estado: () => of({ id: 123 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(EstadoDetailComponent, '')
      .compileComponents();
  });

  describe('OnInit', () => {
    it('Should load estado on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', EstadoDetailComponent);

      // THEN
      expect(instance.estado).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
