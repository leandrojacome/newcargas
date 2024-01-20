import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../notificacao.test-samples';

import { NotificacaoFormService } from './notificacao-form.service';

describe('Notificacao Form Service', () => {
  let service: NotificacaoFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(NotificacaoFormService);
  });

  describe('Service methods', () => {
    describe('createNotificacaoFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createNotificacaoFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            tipo: expect.any(Object),
            email: expect.any(Object),
            telefone: expect.any(Object),
            assunto: expect.any(Object),
            mensagem: expect.any(Object),
            dataHoraEnvio: expect.any(Object),
            dataHoraLeitura: expect.any(Object),
            dataCadastro: expect.any(Object),
            dataAtualizacao: expect.any(Object),
            lido: expect.any(Object),
            dataLeitura: expect.any(Object),
            removido: expect.any(Object),
            dataRemocao: expect.any(Object),
            usuarioRemocao: expect.any(Object),
            embarcador: expect.any(Object),
            transportadora: expect.any(Object),
          }),
        );
      });

      it('passing INotificacao should create a new form with FormGroup', () => {
        const formGroup = service.createNotificacaoFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            tipo: expect.any(Object),
            email: expect.any(Object),
            telefone: expect.any(Object),
            assunto: expect.any(Object),
            mensagem: expect.any(Object),
            dataHoraEnvio: expect.any(Object),
            dataHoraLeitura: expect.any(Object),
            dataCadastro: expect.any(Object),
            dataAtualizacao: expect.any(Object),
            lido: expect.any(Object),
            dataLeitura: expect.any(Object),
            removido: expect.any(Object),
            dataRemocao: expect.any(Object),
            usuarioRemocao: expect.any(Object),
            embarcador: expect.any(Object),
            transportadora: expect.any(Object),
          }),
        );
      });
    });

    describe('getNotificacao', () => {
      it('should return NewNotificacao for default Notificacao initial value', () => {
        const formGroup = service.createNotificacaoFormGroup(sampleWithNewData);

        const notificacao = service.getNotificacao(formGroup) as any;

        expect(notificacao).toMatchObject(sampleWithNewData);
      });

      it('should return NewNotificacao for empty Notificacao initial value', () => {
        const formGroup = service.createNotificacaoFormGroup();

        const notificacao = service.getNotificacao(formGroup) as any;

        expect(notificacao).toMatchObject({});
      });

      it('should return INotificacao', () => {
        const formGroup = service.createNotificacaoFormGroup(sampleWithRequiredData);

        const notificacao = service.getNotificacao(formGroup) as any;

        expect(notificacao).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing INotificacao should not enable id FormControl', () => {
        const formGroup = service.createNotificacaoFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewNotificacao should disable id FormControl', () => {
        const formGroup = service.createNotificacaoFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
