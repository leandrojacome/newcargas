import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../tomada-preco.test-samples';

import { TomadaPrecoFormService } from './tomada-preco-form.service';

describe('TomadaPreco Form Service', () => {
  let service: TomadaPrecoFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(TomadaPrecoFormService);
  });

  describe('Service methods', () => {
    describe('createTomadaPrecoFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createTomadaPrecoFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            dataHoraEnvio: expect.any(Object),
            prazoResposta: expect.any(Object),
            valorTotal: expect.any(Object),
            observacao: expect.any(Object),
            aprovado: expect.any(Object),
            cancelado: expect.any(Object),
            removido: expect.any(Object),
            createdBy: expect.any(Object),
            createdDate: expect.any(Object),
            lastModifiedBy: expect.any(Object),
            lastModifiedDate: expect.any(Object),
            contratacao: expect.any(Object),
            transportadora: expect.any(Object),
            roteirizacao: expect.any(Object),
          }),
        );
      });

      it('passing ITomadaPreco should create a new form with FormGroup', () => {
        const formGroup = service.createTomadaPrecoFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            dataHoraEnvio: expect.any(Object),
            prazoResposta: expect.any(Object),
            valorTotal: expect.any(Object),
            observacao: expect.any(Object),
            aprovado: expect.any(Object),
            cancelado: expect.any(Object),
            removido: expect.any(Object),
            createdBy: expect.any(Object),
            createdDate: expect.any(Object),
            lastModifiedBy: expect.any(Object),
            lastModifiedDate: expect.any(Object),
            contratacao: expect.any(Object),
            transportadora: expect.any(Object),
            roteirizacao: expect.any(Object),
          }),
        );
      });
    });

    describe('getTomadaPreco', () => {
      it('should return NewTomadaPreco for default TomadaPreco initial value', () => {
        const formGroup = service.createTomadaPrecoFormGroup(sampleWithNewData);

        const tomadaPreco = service.getTomadaPreco(formGroup) as any;

        expect(tomadaPreco).toMatchObject(sampleWithNewData);
      });

      it('should return NewTomadaPreco for empty TomadaPreco initial value', () => {
        const formGroup = service.createTomadaPrecoFormGroup();

        const tomadaPreco = service.getTomadaPreco(formGroup) as any;

        expect(tomadaPreco).toMatchObject({});
      });

      it('should return ITomadaPreco', () => {
        const formGroup = service.createTomadaPrecoFormGroup(sampleWithRequiredData);

        const tomadaPreco = service.getTomadaPreco(formGroup) as any;

        expect(tomadaPreco).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing ITomadaPreco should not enable id FormControl', () => {
        const formGroup = service.createTomadaPrecoFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewTomadaPreco should disable id FormControl', () => {
        const formGroup = service.createTomadaPrecoFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
