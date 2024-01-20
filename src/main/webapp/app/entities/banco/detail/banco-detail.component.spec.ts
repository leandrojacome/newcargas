import { TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness, RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { BancoDetailComponent } from './banco-detail.component';

describe('Banco Management Detail Component', () => {
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [BancoDetailComponent, RouterTestingModule.withRoutes([], { bindToComponentInputs: true })],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              component: BancoDetailComponent,
              resolve: { banco: () => of({ id: 123 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(BancoDetailComponent, '')
      .compileComponents();
  });

  describe('OnInit', () => {
    it('Should load banco on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', BancoDetailComponent);

      // THEN
      expect(instance.banco).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
