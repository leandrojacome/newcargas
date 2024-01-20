import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IRegiao } from '../regiao.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../regiao.test-samples';

import { RegiaoService } from './regiao.service';

const requireRestSample: IRegiao = {
  ...sampleWithRequiredData,
};

describe('Regiao Service', () => {
  let service: RegiaoService;
  let httpMock: HttpTestingController;
  let expectedResult: IRegiao | IRegiao[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(RegiaoService);
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

    it('should create a Regiao', () => {
      const regiao = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(regiao).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Regiao', () => {
      const regiao = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(regiao).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Regiao', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Regiao', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a Regiao', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    it('should handle exceptions for searching a Regiao', () => {
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

    describe('addRegiaoToCollectionIfMissing', () => {
      it('should add a Regiao to an empty array', () => {
        const regiao: IRegiao = sampleWithRequiredData;
        expectedResult = service.addRegiaoToCollectionIfMissing([], regiao);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(regiao);
      });

      it('should not add a Regiao to an array that contains it', () => {
        const regiao: IRegiao = sampleWithRequiredData;
        const regiaoCollection: IRegiao[] = [
          {
            ...regiao,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addRegiaoToCollectionIfMissing(regiaoCollection, regiao);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Regiao to an array that doesn't contain it", () => {
        const regiao: IRegiao = sampleWithRequiredData;
        const regiaoCollection: IRegiao[] = [sampleWithPartialData];
        expectedResult = service.addRegiaoToCollectionIfMissing(regiaoCollection, regiao);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(regiao);
      });

      it('should add only unique Regiao to an array', () => {
        const regiaoArray: IRegiao[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const regiaoCollection: IRegiao[] = [sampleWithRequiredData];
        expectedResult = service.addRegiaoToCollectionIfMissing(regiaoCollection, ...regiaoArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const regiao: IRegiao = sampleWithRequiredData;
        const regiao2: IRegiao = sampleWithPartialData;
        expectedResult = service.addRegiaoToCollectionIfMissing([], regiao, regiao2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(regiao);
        expect(expectedResult).toContain(regiao2);
      });

      it('should accept null and undefined values', () => {
        const regiao: IRegiao = sampleWithRequiredData;
        expectedResult = service.addRegiaoToCollectionIfMissing([], null, regiao, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(regiao);
      });

      it('should return initial array if no Regiao is added', () => {
        const regiaoCollection: IRegiao[] = [sampleWithRequiredData];
        expectedResult = service.addRegiaoToCollectionIfMissing(regiaoCollection, undefined, null);
        expect(expectedResult).toEqual(regiaoCollection);
      });
    });

    describe('compareRegiao', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareRegiao(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareRegiao(entity1, entity2);
        const compareResult2 = service.compareRegiao(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareRegiao(entity1, entity2);
        const compareResult2 = service.compareRegiao(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareRegiao(entity1, entity2);
        const compareResult2 = service.compareRegiao(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
