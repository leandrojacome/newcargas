import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../tipo-veiculo.test-samples';

import { TipoVeiculoFormService } from './tipo-veiculo-form.service';

describe('TipoVeiculo Form Service', () => {
  let service: TipoVeiculoFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(TipoVeiculoFormService);
  });

  describe('Service methods', () => {
    describe('createTipoVeiculoFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createTipoVeiculoFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            nome: expect.any(Object),
            descricao: expect.any(Object),
          }),
        );
      });

      it('passing ITipoVeiculo should create a new form with FormGroup', () => {
        const formGroup = service.createTipoVeiculoFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            nome: expect.any(Object),
            descricao: expect.any(Object),
          }),
        );
      });
    });

    describe('getTipoVeiculo', () => {
      it('should return NewTipoVeiculo for default TipoVeiculo initial value', () => {
        const formGroup = service.createTipoVeiculoFormGroup(sampleWithNewData);

        const tipoVeiculo = service.getTipoVeiculo(formGroup) as any;

        expect(tipoVeiculo).toMatchObject(sampleWithNewData);
      });

      it('should return NewTipoVeiculo for empty TipoVeiculo initial value', () => {
        const formGroup = service.createTipoVeiculoFormGroup();

        const tipoVeiculo = service.getTipoVeiculo(formGroup) as any;

        expect(tipoVeiculo).toMatchObject({});
      });

      it('should return ITipoVeiculo', () => {
        const formGroup = service.createTipoVeiculoFormGroup(sampleWithRequiredData);

        const tipoVeiculo = service.getTipoVeiculo(formGroup) as any;

        expect(tipoVeiculo).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing ITipoVeiculo should not enable id FormControl', () => {
        const formGroup = service.createTipoVeiculoFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewTipoVeiculo should disable id FormControl', () => {
        const formGroup = service.createTipoVeiculoFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
