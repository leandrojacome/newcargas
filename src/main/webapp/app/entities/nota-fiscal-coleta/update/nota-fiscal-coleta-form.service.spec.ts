import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../nota-fiscal-coleta.test-samples';

import { NotaFiscalColetaFormService } from './nota-fiscal-coleta-form.service';

describe('NotaFiscalColeta Form Service', () => {
  let service: NotaFiscalColetaFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(NotaFiscalColetaFormService);
  });

  describe('Service methods', () => {
    describe('createNotaFiscalColetaFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createNotaFiscalColetaFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            numero: expect.any(Object),
            serie: expect.any(Object),
            remetente: expect.any(Object),
            destinatario: expect.any(Object),
            metroCubico: expect.any(Object),
            quantidade: expect.any(Object),
            peso: expect.any(Object),
            dataEmissao: expect.any(Object),
            dataSaida: expect.any(Object),
            valorTotal: expect.any(Object),
            pesoTotal: expect.any(Object),
            quantidadeTotal: expect.any(Object),
            observacao: expect.any(Object),
            dataCadastro: expect.any(Object),
            dataAtualizacao: expect.any(Object),
            solicitacaoColeta: expect.any(Object),
          }),
        );
      });

      it('passing INotaFiscalColeta should create a new form with FormGroup', () => {
        const formGroup = service.createNotaFiscalColetaFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            numero: expect.any(Object),
            serie: expect.any(Object),
            remetente: expect.any(Object),
            destinatario: expect.any(Object),
            metroCubico: expect.any(Object),
            quantidade: expect.any(Object),
            peso: expect.any(Object),
            dataEmissao: expect.any(Object),
            dataSaida: expect.any(Object),
            valorTotal: expect.any(Object),
            pesoTotal: expect.any(Object),
            quantidadeTotal: expect.any(Object),
            observacao: expect.any(Object),
            dataCadastro: expect.any(Object),
            dataAtualizacao: expect.any(Object),
            solicitacaoColeta: expect.any(Object),
          }),
        );
      });
    });

    describe('getNotaFiscalColeta', () => {
      it('should return NewNotaFiscalColeta for default NotaFiscalColeta initial value', () => {
        const formGroup = service.createNotaFiscalColetaFormGroup(sampleWithNewData);

        const notaFiscalColeta = service.getNotaFiscalColeta(formGroup) as any;

        expect(notaFiscalColeta).toMatchObject(sampleWithNewData);
      });

      it('should return NewNotaFiscalColeta for empty NotaFiscalColeta initial value', () => {
        const formGroup = service.createNotaFiscalColetaFormGroup();

        const notaFiscalColeta = service.getNotaFiscalColeta(formGroup) as any;

        expect(notaFiscalColeta).toMatchObject({});
      });

      it('should return INotaFiscalColeta', () => {
        const formGroup = service.createNotaFiscalColetaFormGroup(sampleWithRequiredData);

        const notaFiscalColeta = service.getNotaFiscalColeta(formGroup) as any;

        expect(notaFiscalColeta).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing INotaFiscalColeta should not enable id FormControl', () => {
        const formGroup = service.createNotaFiscalColetaFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewNotaFiscalColeta should disable id FormControl', () => {
        const formGroup = service.createNotaFiscalColetaFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
