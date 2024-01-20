import { TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness, RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { TransportadoraDetailComponent } from './transportadora-detail.component';

describe('Transportadora Management Detail Component', () => {
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [TransportadoraDetailComponent, RouterTestingModule.withRoutes([], { bindToComponentInputs: true })],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              component: TransportadoraDetailComponent,
              resolve: { transportadora: () => of({ id: 123 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(TransportadoraDetailComponent, '')
      .compileComponents();
  });

  describe('OnInit', () => {
    it('Should load transportadora on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', TransportadoraDetailComponent);

      // THEN
      expect(instance.transportadora).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
