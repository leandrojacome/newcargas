import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IHistoricoStatusColeta } from '../historico-status-coleta.model';
import {
  sampleWithRequiredData,
  sampleWithNewData,
  sampleWithPartialData,
  sampleWithFullData,
} from '../historico-status-coleta.test-samples';

import { HistoricoStatusColetaService, RestHistoricoStatusColeta } from './historico-status-coleta.service';

const requireRestSample: RestHistoricoStatusColeta = {
  ...sampleWithRequiredData,
  dataCriacao: sampleWithRequiredData.dataCriacao?.toJSON(),
  createdDate: sampleWithRequiredData.createdDate?.toJSON(),
  lastModifiedDate: sampleWithRequiredData.lastModifiedDate?.toJSON(),
};

describe('HistoricoStatusColeta Service', () => {
  let service: HistoricoStatusColetaService;
  let httpMock: HttpTestingController;
  let expectedResult: IHistoricoStatusColeta | IHistoricoStatusColeta[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(HistoricoStatusColetaService);
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

    it('should create a HistoricoStatusColeta', () => {
      const historicoStatusColeta = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(historicoStatusColeta).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a HistoricoStatusColeta', () => {
      const historicoStatusColeta = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(historicoStatusColeta).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a HistoricoStatusColeta', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of HistoricoStatusColeta', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a HistoricoStatusColeta', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    it('should handle exceptions for searching a HistoricoStatusColeta', () => {
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

    describe('addHistoricoStatusColetaToCollectionIfMissing', () => {
      it('should add a HistoricoStatusColeta to an empty array', () => {
        const historicoStatusColeta: IHistoricoStatusColeta = sampleWithRequiredData;
        expectedResult = service.addHistoricoStatusColetaToCollectionIfMissing([], historicoStatusColeta);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(historicoStatusColeta);
      });

      it('should not add a HistoricoStatusColeta to an array that contains it', () => {
        const historicoStatusColeta: IHistoricoStatusColeta = sampleWithRequiredData;
        const historicoStatusColetaCollection: IHistoricoStatusColeta[] = [
          {
            ...historicoStatusColeta,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addHistoricoStatusColetaToCollectionIfMissing(historicoStatusColetaCollection, historicoStatusColeta);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a HistoricoStatusColeta to an array that doesn't contain it", () => {
        const historicoStatusColeta: IHistoricoStatusColeta = sampleWithRequiredData;
        const historicoStatusColetaCollection: IHistoricoStatusColeta[] = [sampleWithPartialData];
        expectedResult = service.addHistoricoStatusColetaToCollectionIfMissing(historicoStatusColetaCollection, historicoStatusColeta);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(historicoStatusColeta);
      });

      it('should add only unique HistoricoStatusColeta to an array', () => {
        const historicoStatusColetaArray: IHistoricoStatusColeta[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const historicoStatusColetaCollection: IHistoricoStatusColeta[] = [sampleWithRequiredData];
        expectedResult = service.addHistoricoStatusColetaToCollectionIfMissing(
          historicoStatusColetaCollection,
          ...historicoStatusColetaArray,
        );
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const historicoStatusColeta: IHistoricoStatusColeta = sampleWithRequiredData;
        const historicoStatusColeta2: IHistoricoStatusColeta = sampleWithPartialData;
        expectedResult = service.addHistoricoStatusColetaToCollectionIfMissing([], historicoStatusColeta, historicoStatusColeta2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(historicoStatusColeta);
        expect(expectedResult).toContain(historicoStatusColeta2);
      });

      it('should accept null and undefined values', () => {
        const historicoStatusColeta: IHistoricoStatusColeta = sampleWithRequiredData;
        expectedResult = service.addHistoricoStatusColetaToCollectionIfMissing([], null, historicoStatusColeta, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(historicoStatusColeta);
      });

      it('should return initial array if no HistoricoStatusColeta is added', () => {
        const historicoStatusColetaCollection: IHistoricoStatusColeta[] = [sampleWithRequiredData];
        expectedResult = service.addHistoricoStatusColetaToCollectionIfMissing(historicoStatusColetaCollection, undefined, null);
        expect(expectedResult).toEqual(historicoStatusColetaCollection);
      });
    });

    describe('compareHistoricoStatusColeta', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareHistoricoStatusColeta(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareHistoricoStatusColeta(entity1, entity2);
        const compareResult2 = service.compareHistoricoStatusColeta(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareHistoricoStatusColeta(entity1, entity2);
        const compareResult2 = service.compareHistoricoStatusColeta(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareHistoricoStatusColeta(entity1, entity2);
        const compareResult2 = service.compareHistoricoStatusColeta(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
