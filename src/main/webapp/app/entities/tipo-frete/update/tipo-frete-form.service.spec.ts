import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../tipo-frete.test-samples';

import { TipoFreteFormService } from './tipo-frete-form.service';

describe('TipoFrete Form Service', () => {
  let service: TipoFreteFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(TipoFreteFormService);
  });

  describe('Service methods', () => {
    describe('createTipoFreteFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createTipoFreteFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            nome: expect.any(Object),
            descricao: expect.any(Object),
          }),
        );
      });

      it('passing ITipoFrete should create a new form with FormGroup', () => {
        const formGroup = service.createTipoFreteFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            nome: expect.any(Object),
            descricao: expect.any(Object),
          }),
        );
      });
    });

    describe('getTipoFrete', () => {
      it('should return NewTipoFrete for default TipoFrete initial value', () => {
        const formGroup = service.createTipoFreteFormGroup(sampleWithNewData);

        const tipoFrete = service.getTipoFrete(formGroup) as any;

        expect(tipoFrete).toMatchObject(sampleWithNewData);
      });

      it('should return NewTipoFrete for empty TipoFrete initial value', () => {
        const formGroup = service.createTipoFreteFormGroup();

        const tipoFrete = service.getTipoFrete(formGroup) as any;

        expect(tipoFrete).toMatchObject({});
      });

      it('should return ITipoFrete', () => {
        const formGroup = service.createTipoFreteFormGroup(sampleWithRequiredData);

        const tipoFrete = service.getTipoFrete(formGroup) as any;

        expect(tipoFrete).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing ITipoFrete should not enable id FormControl', () => {
        const formGroup = service.createTipoFreteFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewTipoFrete should disable id FormControl', () => {
        const formGroup = service.createTipoFreteFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
