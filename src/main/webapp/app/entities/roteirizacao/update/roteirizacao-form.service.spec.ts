import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../roteirizacao.test-samples';

import { RoteirizacaoFormService } from './roteirizacao-form.service';

describe('Roteirizacao Form Service', () => {
  let service: RoteirizacaoFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(RoteirizacaoFormService);
  });

  describe('Service methods', () => {
    describe('createRoteirizacaoFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createRoteirizacaoFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            dataHoraPrimeiraColeta: expect.any(Object),
            dataHoraUltimaColeta: expect.any(Object),
            dataHoraPrimeiraEntrega: expect.any(Object),
            dataHoraUltimaEntrega: expect.any(Object),
            valorTotal: expect.any(Object),
            observacao: expect.any(Object),
            cancelado: expect.any(Object),
            removido: expect.any(Object),
            createdBy: expect.any(Object),
            createdDate: expect.any(Object),
            lastModifiedBy: expect.any(Object),
            lastModifiedDate: expect.any(Object),
            statusColeta: expect.any(Object),
          }),
        );
      });

      it('passing IRoteirizacao should create a new form with FormGroup', () => {
        const formGroup = service.createRoteirizacaoFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            dataHoraPrimeiraColeta: expect.any(Object),
            dataHoraUltimaColeta: expect.any(Object),
            dataHoraPrimeiraEntrega: expect.any(Object),
            dataHoraUltimaEntrega: expect.any(Object),
            valorTotal: expect.any(Object),
            observacao: expect.any(Object),
            cancelado: expect.any(Object),
            removido: expect.any(Object),
            createdBy: expect.any(Object),
            createdDate: expect.any(Object),
            lastModifiedBy: expect.any(Object),
            lastModifiedDate: expect.any(Object),
            statusColeta: expect.any(Object),
          }),
        );
      });
    });

    describe('getRoteirizacao', () => {
      it('should return NewRoteirizacao for default Roteirizacao initial value', () => {
        const formGroup = service.createRoteirizacaoFormGroup(sampleWithNewData);

        const roteirizacao = service.getRoteirizacao(formGroup) as any;

        expect(roteirizacao).toMatchObject(sampleWithNewData);
      });

      it('should return NewRoteirizacao for empty Roteirizacao initial value', () => {
        const formGroup = service.createRoteirizacaoFormGroup();

        const roteirizacao = service.getRoteirizacao(formGroup) as any;

        expect(roteirizacao).toMatchObject({});
      });

      it('should return IRoteirizacao', () => {
        const formGroup = service.createRoteirizacaoFormGroup(sampleWithRequiredData);

        const roteirizacao = service.getRoteirizacao(formGroup) as any;

        expect(roteirizacao).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IRoteirizacao should not enable id FormControl', () => {
        const formGroup = service.createRoteirizacaoFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewRoteirizacao should disable id FormControl', () => {
        const formGroup = service.createRoteirizacaoFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
