import { TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness, RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { NotificacaoDetailComponent } from './notificacao-detail.component';

describe('Notificacao Management Detail Component', () => {
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [NotificacaoDetailComponent, RouterTestingModule.withRoutes([], { bindToComponentInputs: true })],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              component: NotificacaoDetailComponent,
              resolve: { notificacao: () => of({ id: 123 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(NotificacaoDetailComponent, '')
      .compileComponents();
  });

  describe('OnInit', () => {
    it('Should load notificacao on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', NotificacaoDetailComponent);

      // THEN
      expect(instance.notificacao).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
