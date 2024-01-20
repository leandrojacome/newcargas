import { TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness, RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { CidadeDetailComponent } from './cidade-detail.component';

describe('Cidade Management Detail Component', () => {
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [CidadeDetailComponent, RouterTestingModule.withRoutes([], { bindToComponentInputs: true })],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              component: CidadeDetailComponent,
              resolve: { cidade: () => of({ id: 123 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(CidadeDetailComponent, '')
      .compileComponents();
  });

  describe('OnInit', () => {
    it('Should load cidade on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', CidadeDetailComponent);

      // THEN
      expect(instance.cidade).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
