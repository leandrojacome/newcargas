import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../tabela-frete.test-samples';

import { TabelaFreteFormService } from './tabela-frete-form.service';

describe('TabelaFrete Form Service', () => {
  let service: TabelaFreteFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(TabelaFreteFormService);
  });

  describe('Service methods', () => {
    describe('createTabelaFreteFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createTabelaFreteFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            tipo: expect.any(Object),
            nome: expect.any(Object),
            descricao: expect.any(Object),
            leadTime: expect.any(Object),
            freteMinimo: expect.any(Object),
            valorTonelada: expect.any(Object),
            valorMetroCubico: expect.any(Object),
            valorUnidade: expect.any(Object),
            valorKm: expect.any(Object),
            valorAdicional: expect.any(Object),
            valorColeta: expect.any(Object),
            valorEntrega: expect.any(Object),
            valorTotal: expect.any(Object),
            valorKmAdicional: expect.any(Object),
            createdBy: expect.any(Object),
            createdDate: expect.any(Object),
            lastModifiedBy: expect.any(Object),
            lastModifiedDate: expect.any(Object),
            embarcador: expect.any(Object),
            transportadora: expect.any(Object),
            tipoCarga: expect.any(Object),
            tipoFrete: expect.any(Object),
            formaCobranca: expect.any(Object),
            regiaoOrigem: expect.any(Object),
            regiaoDestino: expect.any(Object),
          }),
        );
      });

      it('passing ITabelaFrete should create a new form with FormGroup', () => {
        const formGroup = service.createTabelaFreteFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            tipo: expect.any(Object),
            nome: expect.any(Object),
            descricao: expect.any(Object),
            leadTime: expect.any(Object),
            freteMinimo: expect.any(Object),
            valorTonelada: expect.any(Object),
            valorMetroCubico: expect.any(Object),
            valorUnidade: expect.any(Object),
            valorKm: expect.any(Object),
            valorAdicional: expect.any(Object),
            valorColeta: expect.any(Object),
            valorEntrega: expect.any(Object),
            valorTotal: expect.any(Object),
            valorKmAdicional: expect.any(Object),
            createdBy: expect.any(Object),
            createdDate: expect.any(Object),
            lastModifiedBy: expect.any(Object),
            lastModifiedDate: expect.any(Object),
            embarcador: expect.any(Object),
            transportadora: expect.any(Object),
            tipoCarga: expect.any(Object),
            tipoFrete: expect.any(Object),
            formaCobranca: expect.any(Object),
            regiaoOrigem: expect.any(Object),
            regiaoDestino: expect.any(Object),
          }),
        );
      });
    });

    describe('getTabelaFrete', () => {
      it('should return NewTabelaFrete for default TabelaFrete initial value', () => {
        const formGroup = service.createTabelaFreteFormGroup(sampleWithNewData);

        const tabelaFrete = service.getTabelaFrete(formGroup) as any;

        expect(tabelaFrete).toMatchObject(sampleWithNewData);
      });

      it('should return NewTabelaFrete for empty TabelaFrete initial value', () => {
        const formGroup = service.createTabelaFreteFormGroup();

        const tabelaFrete = service.getTabelaFrete(formGroup) as any;

        expect(tabelaFrete).toMatchObject({});
      });

      it('should return ITabelaFrete', () => {
        const formGroup = service.createTabelaFreteFormGroup(sampleWithRequiredData);

        const tabelaFrete = service.getTabelaFrete(formGroup) as any;

        expect(tabelaFrete).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing ITabelaFrete should not enable id FormControl', () => {
        const formGroup = service.createTabelaFreteFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewTabelaFrete should disable id FormControl', () => {
        const formGroup = service.createTabelaFreteFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
