import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../fatura.test-samples';

import { FaturaFormService } from './fatura-form.service';

describe('Fatura Form Service', () => {
  let service: FaturaFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(FaturaFormService);
  });

  describe('Service methods', () => {
    describe('createFaturaFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createFaturaFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            tipo: expect.any(Object),
            dataFatura: expect.any(Object),
            dataVencimento: expect.any(Object),
            dataPagamento: expect.any(Object),
            numeroParcela: expect.any(Object),
            valorTotal: expect.any(Object),
            observacao: expect.any(Object),
            dataCadastro: expect.any(Object),
            usuarioCadastro: expect.any(Object),
            dataAtualizacao: expect.any(Object),
            usuarioAtualizacao: expect.any(Object),
            cancelado: expect.any(Object),
            dataCancelamento: expect.any(Object),
            usuarioCancelamento: expect.any(Object),
            removido: expect.any(Object),
            dataRemocao: expect.any(Object),
            usuarioRemocao: expect.any(Object),
            embarcador: expect.any(Object),
            transportadora: expect.any(Object),
            contratacao: expect.any(Object),
            formaCobranca: expect.any(Object),
          }),
        );
      });

      it('passing IFatura should create a new form with FormGroup', () => {
        const formGroup = service.createFaturaFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            tipo: expect.any(Object),
            dataFatura: expect.any(Object),
            dataVencimento: expect.any(Object),
            dataPagamento: expect.any(Object),
            numeroParcela: expect.any(Object),
            valorTotal: expect.any(Object),
            observacao: expect.any(Object),
            dataCadastro: expect.any(Object),
            usuarioCadastro: expect.any(Object),
            dataAtualizacao: expect.any(Object),
            usuarioAtualizacao: expect.any(Object),
            cancelado: expect.any(Object),
            dataCancelamento: expect.any(Object),
            usuarioCancelamento: expect.any(Object),
            removido: expect.any(Object),
            dataRemocao: expect.any(Object),
            usuarioRemocao: expect.any(Object),
            embarcador: expect.any(Object),
            transportadora: expect.any(Object),
            contratacao: expect.any(Object),
            formaCobranca: expect.any(Object),
          }),
        );
      });
    });

    describe('getFatura', () => {
      it('should return NewFatura for default Fatura initial value', () => {
        const formGroup = service.createFaturaFormGroup(sampleWithNewData);

        const fatura = service.getFatura(formGroup) as any;

        expect(fatura).toMatchObject(sampleWithNewData);
      });

      it('should return NewFatura for empty Fatura initial value', () => {
        const formGroup = service.createFaturaFormGroup();

        const fatura = service.getFatura(formGroup) as any;

        expect(fatura).toMatchObject({});
      });

      it('should return IFatura', () => {
        const formGroup = service.createFaturaFormGroup(sampleWithRequiredData);

        const fatura = service.getFatura(formGroup) as any;

        expect(fatura).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IFatura should not enable id FormControl', () => {
        const formGroup = service.createFaturaFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewFatura should disable id FormControl', () => {
        const formGroup = service.createFaturaFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
