import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IFatura } from '../fatura.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../fatura.test-samples';

import { FaturaService, RestFatura } from './fatura.service';

const requireRestSample: RestFatura = {
  ...sampleWithRequiredData,
  dataFatura: sampleWithRequiredData.dataFatura?.toJSON(),
  dataVencimento: sampleWithRequiredData.dataVencimento?.toJSON(),
  dataPagamento: sampleWithRequiredData.dataPagamento?.toJSON(),
  createdDate: sampleWithRequiredData.createdDate?.toJSON(),
  lastModifiedDate: sampleWithRequiredData.lastModifiedDate?.toJSON(),
};

describe('Fatura Service', () => {
  let service: FaturaService;
  let httpMock: HttpTestingController;
  let expectedResult: IFatura | IFatura[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(FaturaService);
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

    it('should create a Fatura', () => {
      const fatura = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(fatura).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Fatura', () => {
      const fatura = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(fatura).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Fatura', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Fatura', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a Fatura', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    it('should handle exceptions for searching a Fatura', () => {
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

    describe('addFaturaToCollectionIfMissing', () => {
      it('should add a Fatura to an empty array', () => {
        const fatura: IFatura = sampleWithRequiredData;
        expectedResult = service.addFaturaToCollectionIfMissing([], fatura);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(fatura);
      });

      it('should not add a Fatura to an array that contains it', () => {
        const fatura: IFatura = sampleWithRequiredData;
        const faturaCollection: IFatura[] = [
          {
            ...fatura,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addFaturaToCollectionIfMissing(faturaCollection, fatura);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Fatura to an array that doesn't contain it", () => {
        const fatura: IFatura = sampleWithRequiredData;
        const faturaCollection: IFatura[] = [sampleWithPartialData];
        expectedResult = service.addFaturaToCollectionIfMissing(faturaCollection, fatura);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(fatura);
      });

      it('should add only unique Fatura to an array', () => {
        const faturaArray: IFatura[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const faturaCollection: IFatura[] = [sampleWithRequiredData];
        expectedResult = service.addFaturaToCollectionIfMissing(faturaCollection, ...faturaArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const fatura: IFatura = sampleWithRequiredData;
        const fatura2: IFatura = sampleWithPartialData;
        expectedResult = service.addFaturaToCollectionIfMissing([], fatura, fatura2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(fatura);
        expect(expectedResult).toContain(fatura2);
      });

      it('should accept null and undefined values', () => {
        const fatura: IFatura = sampleWithRequiredData;
        expectedResult = service.addFaturaToCollectionIfMissing([], null, fatura, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(fatura);
      });

      it('should return initial array if no Fatura is added', () => {
        const faturaCollection: IFatura[] = [sampleWithRequiredData];
        expectedResult = service.addFaturaToCollectionIfMissing(faturaCollection, undefined, null);
        expect(expectedResult).toEqual(faturaCollection);
      });
    });

    describe('compareFatura', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareFatura(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareFatura(entity1, entity2);
        const compareResult2 = service.compareFatura(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareFatura(entity1, entity2);
        const compareResult2 = service.compareFatura(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareFatura(entity1, entity2);
        const compareResult2 = service.compareFatura(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
