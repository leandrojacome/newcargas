import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { ITipoVeiculo } from '../tipo-veiculo.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../tipo-veiculo.test-samples';

import { TipoVeiculoService, RestTipoVeiculo } from './tipo-veiculo.service';

const requireRestSample: RestTipoVeiculo = {
  ...sampleWithRequiredData,
  createdDate: sampleWithRequiredData.createdDate?.toJSON(),
  lastModifiedDate: sampleWithRequiredData.lastModifiedDate?.toJSON(),
};

describe('TipoVeiculo Service', () => {
  let service: TipoVeiculoService;
  let httpMock: HttpTestingController;
  let expectedResult: ITipoVeiculo | ITipoVeiculo[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(TipoVeiculoService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should create a TipoVeiculo', () => {
      const tipoVeiculo = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(tipoVeiculo).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a TipoVeiculo', () => {
      const tipoVeiculo = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(tipoVeiculo).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a TipoVeiculo', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of TipoVeiculo', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a TipoVeiculo', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    it('should handle exceptions for searching a TipoVeiculo', () => {
      const queryObject: any = {
        page: 0,
        size: 20,
        query: '',
        sort: [],
      };
      service.search(queryObject).subscribe(() => expectedResult);

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(null, { status: 500, statusText: 'Internal Server Error' });
      expect(expectedResult).toBe(null);
    });

    describe('addTipoVeiculoToCollectionIfMissing', () => {
      it('should add a TipoVeiculo to an empty array', () => {
        const tipoVeiculo: ITipoVeiculo = sampleWithRequiredData;
        expectedResult = service.addTipoVeiculoToCollectionIfMissing([], tipoVeiculo);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(tipoVeiculo);
      });

      it('should not add a TipoVeiculo to an array that contains it', () => {
        const tipoVeiculo: ITipoVeiculo = sampleWithRequiredData;
        const tipoVeiculoCollection: ITipoVeiculo[] = [
          {
            ...tipoVeiculo,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addTipoVeiculoToCollectionIfMissing(tipoVeiculoCollection, tipoVeiculo);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a TipoVeiculo to an array that doesn't contain it", () => {
        const tipoVeiculo: ITipoVeiculo = sampleWithRequiredData;
        const tipoVeiculoCollection: ITipoVeiculo[] = [sampleWithPartialData];
        expectedResult = service.addTipoVeiculoToCollectionIfMissing(tipoVeiculoCollection, tipoVeiculo);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(tipoVeiculo);
      });

      it('should add only unique TipoVeiculo to an array', () => {
        const tipoVeiculoArray: ITipoVeiculo[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const tipoVeiculoCollection: ITipoVeiculo[] = [sampleWithRequiredData];
        expectedResult = service.addTipoVeiculoToCollectionIfMissing(tipoVeiculoCollection, ...tipoVeiculoArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const tipoVeiculo: ITipoVeiculo = sampleWithRequiredData;
        const tipoVeiculo2: ITipoVeiculo = sampleWithPartialData;
        expectedResult = service.addTipoVeiculoToCollectionIfMissing([], tipoVeiculo, tipoVeiculo2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(tipoVeiculo);
        expect(expectedResult).toContain(tipoVeiculo2);
      });

      it('should accept null and undefined values', () => {
        const tipoVeiculo: ITipoVeiculo = sampleWithRequiredData;
        expectedResult = service.addTipoVeiculoToCollectionIfMissing([], null, tipoVeiculo, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(tipoVeiculo);
      });

      it('should return initial array if no TipoVeiculo is added', () => {
        const tipoVeiculoCollection: ITipoVeiculo[] = [sampleWithRequiredData];
        expectedResult = service.addTipoVeiculoToCollectionIfMissing(tipoVeiculoCollection, undefined, null);
        expect(expectedResult).toEqual(tipoVeiculoCollection);
      });
    });

    describe('compareTipoVeiculo', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareTipoVeiculo(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareTipoVeiculo(entity1, entity2);
        const compareResult2 = service.compareTipoVeiculo(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareTipoVeiculo(entity1, entity2);
        const compareResult2 = service.compareTipoVeiculo(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareTipoVeiculo(entity1, entity2);
        const compareResult2 = service.compareTipoVeiculo(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
