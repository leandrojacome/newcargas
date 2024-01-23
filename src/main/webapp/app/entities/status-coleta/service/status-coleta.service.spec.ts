import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IStatusColeta } from '../status-coleta.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../status-coleta.test-samples';

import { StatusColetaService, RestStatusColeta } from './status-coleta.service';

const requireRestSample: RestStatusColeta = {
  ...sampleWithRequiredData,
  createdDate: sampleWithRequiredData.createdDate?.toJSON(),
  lastModifiedDate: sampleWithRequiredData.lastModifiedDate?.toJSON(),
};

describe('StatusColeta Service', () => {
  let service: StatusColetaService;
  let httpMock: HttpTestingController;
  let expectedResult: IStatusColeta | IStatusColeta[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(StatusColetaService);
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

    it('should create a StatusColeta', () => {
      const statusColeta = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(statusColeta).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a StatusColeta', () => {
      const statusColeta = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(statusColeta).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a StatusColeta', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of StatusColeta', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a StatusColeta', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    it('should handle exceptions for searching a StatusColeta', () => {
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

    describe('addStatusColetaToCollectionIfMissing', () => {
      it('should add a StatusColeta to an empty array', () => {
        const statusColeta: IStatusColeta = sampleWithRequiredData;
        expectedResult = service.addStatusColetaToCollectionIfMissing([], statusColeta);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(statusColeta);
      });

      it('should not add a StatusColeta to an array that contains it', () => {
        const statusColeta: IStatusColeta = sampleWithRequiredData;
        const statusColetaCollection: IStatusColeta[] = [
          {
            ...statusColeta,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addStatusColetaToCollectionIfMissing(statusColetaCollection, statusColeta);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a StatusColeta to an array that doesn't contain it", () => {
        const statusColeta: IStatusColeta = sampleWithRequiredData;
        const statusColetaCollection: IStatusColeta[] = [sampleWithPartialData];
        expectedResult = service.addStatusColetaToCollectionIfMissing(statusColetaCollection, statusColeta);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(statusColeta);
      });

      it('should add only unique StatusColeta to an array', () => {
        const statusColetaArray: IStatusColeta[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const statusColetaCollection: IStatusColeta[] = [sampleWithRequiredData];
        expectedResult = service.addStatusColetaToCollectionIfMissing(statusColetaCollection, ...statusColetaArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const statusColeta: IStatusColeta = sampleWithRequiredData;
        const statusColeta2: IStatusColeta = sampleWithPartialData;
        expectedResult = service.addStatusColetaToCollectionIfMissing([], statusColeta, statusColeta2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(statusColeta);
        expect(expectedResult).toContain(statusColeta2);
      });

      it('should accept null and undefined values', () => {
        const statusColeta: IStatusColeta = sampleWithRequiredData;
        expectedResult = service.addStatusColetaToCollectionIfMissing([], null, statusColeta, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(statusColeta);
      });

      it('should return initial array if no StatusColeta is added', () => {
        const statusColetaCollection: IStatusColeta[] = [sampleWithRequiredData];
        expectedResult = service.addStatusColetaToCollectionIfMissing(statusColetaCollection, undefined, null);
        expect(expectedResult).toEqual(statusColetaCollection);
      });
    });

    describe('compareStatusColeta', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareStatusColeta(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareStatusColeta(entity1, entity2);
        const compareResult2 = service.compareStatusColeta(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareStatusColeta(entity1, entity2);
        const compareResult2 = service.compareStatusColeta(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareStatusColeta(entity1, entity2);
        const compareResult2 = service.compareStatusColeta(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
