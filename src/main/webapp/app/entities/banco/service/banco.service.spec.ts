import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IBanco } from '../banco.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../banco.test-samples';

import { BancoService, RestBanco } from './banco.service';

const requireRestSample: RestBanco = {
  ...sampleWithRequiredData,
  createdDate: sampleWithRequiredData.createdDate?.toJSON(),
  lastModifiedDate: sampleWithRequiredData.lastModifiedDate?.toJSON(),
};

describe('Banco Service', () => {
  let service: BancoService;
  let httpMock: HttpTestingController;
  let expectedResult: IBanco | IBanco[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(BancoService);
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

    it('should create a Banco', () => {
      const banco = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(banco).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Banco', () => {
      const banco = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(banco).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Banco', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Banco', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a Banco', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    it('should handle exceptions for searching a Banco', () => {
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

    describe('addBancoToCollectionIfMissing', () => {
      it('should add a Banco to an empty array', () => {
        const banco: IBanco = sampleWithRequiredData;
        expectedResult = service.addBancoToCollectionIfMissing([], banco);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(banco);
      });

      it('should not add a Banco to an array that contains it', () => {
        const banco: IBanco = sampleWithRequiredData;
        const bancoCollection: IBanco[] = [
          {
            ...banco,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addBancoToCollectionIfMissing(bancoCollection, banco);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Banco to an array that doesn't contain it", () => {
        const banco: IBanco = sampleWithRequiredData;
        const bancoCollection: IBanco[] = [sampleWithPartialData];
        expectedResult = service.addBancoToCollectionIfMissing(bancoCollection, banco);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(banco);
      });

      it('should add only unique Banco to an array', () => {
        const bancoArray: IBanco[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const bancoCollection: IBanco[] = [sampleWithRequiredData];
        expectedResult = service.addBancoToCollectionIfMissing(bancoCollection, ...bancoArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const banco: IBanco = sampleWithRequiredData;
        const banco2: IBanco = sampleWithPartialData;
        expectedResult = service.addBancoToCollectionIfMissing([], banco, banco2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(banco);
        expect(expectedResult).toContain(banco2);
      });

      it('should accept null and undefined values', () => {
        const banco: IBanco = sampleWithRequiredData;
        expectedResult = service.addBancoToCollectionIfMissing([], null, banco, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(banco);
      });

      it('should return initial array if no Banco is added', () => {
        const bancoCollection: IBanco[] = [sampleWithRequiredData];
        expectedResult = service.addBancoToCollectionIfMissing(bancoCollection, undefined, null);
        expect(expectedResult).toEqual(bancoCollection);
      });
    });

    describe('compareBanco', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareBanco(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareBanco(entity1, entity2);
        const compareResult2 = service.compareBanco(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareBanco(entity1, entity2);
        const compareResult2 = service.compareBanco(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareBanco(entity1, entity2);
        const compareResult2 = service.compareBanco(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
