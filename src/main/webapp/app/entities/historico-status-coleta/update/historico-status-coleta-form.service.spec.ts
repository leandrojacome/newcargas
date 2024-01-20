import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../historico-status-coleta.test-samples';

import { HistoricoStatusColetaFormService } from './historico-status-coleta-form.service';

describe('HistoricoStatusColeta Form Service', () => {
  let service: HistoricoStatusColetaFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(HistoricoStatusColetaFormService);
  });

  describe('Service methods', () => {
    describe('createHistoricoStatusColetaFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createHistoricoStatusColetaFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            dataCriacao: expect.any(Object),
            observacao: expect.any(Object),
            solicitacaoColeta: expect.any(Object),
            roteirizacao: expect.any(Object),
            statusColetaOrigem: expect.any(Object),
            statusColetaDestino: expect.any(Object),
          }),
        );
      });

      it('passing IHistoricoStatusColeta should create a new form with FormGroup', () => {
        const formGroup = service.createHistoricoStatusColetaFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            dataCriacao: expect.any(Object),
            observacao: expect.any(Object),
            solicitacaoColeta: expect.any(Object),
            roteirizacao: expect.any(Object),
            statusColetaOrigem: expect.any(Object),
            statusColetaDestino: expect.any(Object),
          }),
        );
      });
    });

    describe('getHistoricoStatusColeta', () => {
      it('should return NewHistoricoStatusColeta for default HistoricoStatusColeta initial value', () => {
        const formGroup = service.createHistoricoStatusColetaFormGroup(sampleWithNewData);

        const historicoStatusColeta = service.getHistoricoStatusColeta(formGroup) as any;

        expect(historicoStatusColeta).toMatchObject(sampleWithNewData);
      });

      it('should return NewHistoricoStatusColeta for empty HistoricoStatusColeta initial value', () => {
        const formGroup = service.createHistoricoStatusColetaFormGroup();

        const historicoStatusColeta = service.getHistoricoStatusColeta(formGroup) as any;

        expect(historicoStatusColeta).toMatchObject({});
      });

      it('should return IHistoricoStatusColeta', () => {
        const formGroup = service.createHistoricoStatusColetaFormGroup(sampleWithRequiredData);

        const historicoStatusColeta = service.getHistoricoStatusColeta(formGroup) as any;

        expect(historicoStatusColeta).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IHistoricoStatusColeta should not enable id FormControl', () => {
        const formGroup = service.createHistoricoStatusColetaFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewHistoricoStatusColeta should disable id FormControl', () => {
        const formGroup = service.createHistoricoStatusColetaFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
