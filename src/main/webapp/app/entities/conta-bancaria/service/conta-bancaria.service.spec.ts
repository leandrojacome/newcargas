import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IContaBancaria } from '../conta-bancaria.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../conta-bancaria.test-samples';

import { ContaBancariaService, RestContaBancaria } from './conta-bancaria.service';

const requireRestSample: RestContaBancaria = {
  ...sampleWithRequiredData,
  dataCadastro: sampleWithRequiredData.dataCadastro?.toJSON(),
  dataAtualizacao: sampleWithRequiredData.dataAtualizacao?.toJSON(),
};

describe('ContaBancaria Service', () => {
  let service: ContaBancariaService;
  let httpMock: HttpTestingController;
  let expectedResult: IContaBancaria | IContaBancaria[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(ContaBancariaService);
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

    it('should create a ContaBancaria', () => {
      const contaBancaria = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(contaBancaria).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a ContaBancaria', () => {
      const contaBancaria = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(contaBancaria).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a ContaBancaria', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of ContaBancaria', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a ContaBancaria', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    it('should handle exceptions for searching a ContaBancaria', () => {
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

    describe('addContaBancariaToCollectionIfMissing', () => {
      it('should add a ContaBancaria to an empty array', () => {
        const contaBancaria: IContaBancaria = sampleWithRequiredData;
        expectedResult = service.addContaBancariaToCollectionIfMissing([], contaBancaria);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(contaBancaria);
      });

      it('should not add a ContaBancaria to an array that contains it', () => {
        const contaBancaria: IContaBancaria = sampleWithRequiredData;
        const contaBancariaCollection: IContaBancaria[] = [
          {
            ...contaBancaria,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addContaBancariaToCollectionIfMissing(contaBancariaCollection, contaBancaria);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a ContaBancaria to an array that doesn't contain it", () => {
        const contaBancaria: IContaBancaria = sampleWithRequiredData;
        const contaBancariaCollection: IContaBancaria[] = [sampleWithPartialData];
        expectedResult = service.addContaBancariaToCollectionIfMissing(contaBancariaCollection, contaBancaria);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(contaBancaria);
      });

      it('should add only unique ContaBancaria to an array', () => {
        const contaBancariaArray: IContaBancaria[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const contaBancariaCollection: IContaBancaria[] = [sampleWithRequiredData];
        expectedResult = service.addContaBancariaToCollectionIfMissing(contaBancariaCollection, ...contaBancariaArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const contaBancaria: IContaBancaria = sampleWithRequiredData;
        const contaBancaria2: IContaBancaria = sampleWithPartialData;
        expectedResult = service.addContaBancariaToCollectionIfMissing([], contaBancaria, contaBancaria2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(contaBancaria);
        expect(expectedResult).toContain(contaBancaria2);
      });

      it('should accept null and undefined values', () => {
        const contaBancaria: IContaBancaria = sampleWithRequiredData;
        expectedResult = service.addContaBancariaToCollectionIfMissing([], null, contaBancaria, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(contaBancaria);
      });

      it('should return initial array if no ContaBancaria is added', () => {
        const contaBancariaCollection: IContaBancaria[] = [sampleWithRequiredData];
        expectedResult = service.addContaBancariaToCollectionIfMissing(contaBancariaCollection, undefined, null);
        expect(expectedResult).toEqual(contaBancariaCollection);
      });
    });

    describe('compareContaBancaria', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareContaBancaria(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareContaBancaria(entity1, entity2);
        const compareResult2 = service.compareContaBancaria(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareContaBancaria(entity1, entity2);
        const compareResult2 = service.compareContaBancaria(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareContaBancaria(entity1, entity2);
        const compareResult2 = service.compareContaBancaria(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
