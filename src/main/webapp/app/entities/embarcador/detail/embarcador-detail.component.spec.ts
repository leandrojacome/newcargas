import { TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness, RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { EmbarcadorDetailComponent } from './embarcador-detail.component';

describe('Embarcador Management Detail Component', () => {
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [EmbarcadorDetailComponent, RouterTestingModule.withRoutes([], { bindToComponentInputs: true })],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              component: EmbarcadorDetailComponent,
              resolve: { embarcador: () => of({ id: 123 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(EmbarcadorDetailComponent, '')
      .compileComponents();
  });

  describe('OnInit', () => {
    it('Should load embarcador on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', EmbarcadorDetailComponent);

      // THEN
      expect(instance.embarcador).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
