import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../solicitacao-coleta.test-samples';

import { SolicitacaoColetaFormService } from './solicitacao-coleta-form.service';

describe('SolicitacaoColeta Form Service', () => {
  let service: SolicitacaoColetaFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(SolicitacaoColetaFormService);
  });

  describe('Service methods', () => {
    describe('createSolicitacaoColetaFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createSolicitacaoColetaFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            coletado: expect.any(Object),
            dataHoraColeta: expect.any(Object),
            entregue: expect.any(Object),
            dataHoraEntrega: expect.any(Object),
            valorTotal: expect.any(Object),
            observacao: expect.any(Object),
            cancelado: expect.any(Object),
            removido: expect.any(Object),
            createdBy: expect.any(Object),
            createdDate: expect.any(Object),
            lastModifiedBy: expect.any(Object),
            lastModifiedDate: expect.any(Object),
            embarcador: expect.any(Object),
            statusColeta: expect.any(Object),
            roteirizacao: expect.any(Object),
            tipoVeiculo: expect.any(Object),
          }),
        );
      });

      it('passing ISolicitacaoColeta should create a new form with FormGroup', () => {
        const formGroup = service.createSolicitacaoColetaFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            coletado: expect.any(Object),
            dataHoraColeta: expect.any(Object),
            entregue: expect.any(Object),
            dataHoraEntrega: expect.any(Object),
            valorTotal: expect.any(Object),
            observacao: expect.any(Object),
            cancelado: expect.any(Object),
            removido: expect.any(Object),
            createdBy: expect.any(Object),
            createdDate: expect.any(Object),
            lastModifiedBy: expect.any(Object),
            lastModifiedDate: expect.any(Object),
            embarcador: expect.any(Object),
            statusColeta: expect.any(Object),
            roteirizacao: expect.any(Object),
            tipoVeiculo: expect.any(Object),
          }),
        );
      });
    });

    describe('getSolicitacaoColeta', () => {
      it('should return NewSolicitacaoColeta for default SolicitacaoColeta initial value', () => {
        const formGroup = service.createSolicitacaoColetaFormGroup(sampleWithNewData);

        const solicitacaoColeta = service.getSolicitacaoColeta(formGroup) as any;

        expect(solicitacaoColeta).toMatchObject(sampleWithNewData);
      });

      it('should return NewSolicitacaoColeta for empty SolicitacaoColeta initial value', () => {
        const formGroup = service.createSolicitacaoColetaFormGroup();

        const solicitacaoColeta = service.getSolicitacaoColeta(formGroup) as any;

        expect(solicitacaoColeta).toMatchObject({});
      });

      it('should return ISolicitacaoColeta', () => {
        const formGroup = service.createSolicitacaoColetaFormGroup(sampleWithRequiredData);

        const solicitacaoColeta = service.getSolicitacaoColeta(formGroup) as any;

        expect(solicitacaoColeta).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing ISolicitacaoColeta should not enable id FormControl', () => {
        const formGroup = service.createSolicitacaoColetaFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewSolicitacaoColeta should disable id FormControl', () => {
        const formGroup = service.createSolicitacaoColetaFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
