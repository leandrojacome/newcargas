import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { ISolicitacaoColeta } from '../solicitacao-coleta.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../solicitacao-coleta.test-samples';

import { SolicitacaoColetaService, RestSolicitacaoColeta } from './solicitacao-coleta.service';

const requireRestSample: RestSolicitacaoColeta = {
  ...sampleWithRequiredData,
  dataHoraColeta: sampleWithRequiredData.dataHoraColeta?.toJSON(),
  dataHoraEntrega: sampleWithRequiredData.dataHoraEntrega?.toJSON(),
  createdDate: sampleWithRequiredData.createdDate?.toJSON(),
  lastModifiedDate: sampleWithRequiredData.lastModifiedDate?.toJSON(),
};

describe('SolicitacaoColeta Service', () => {
  let service: SolicitacaoColetaService;
  let httpMock: HttpTestingController;
  let expectedResult: ISolicitacaoColeta | ISolicitacaoColeta[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(SolicitacaoColetaService);
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

    it('should create a SolicitacaoColeta', () => {
      const solicitacaoColeta = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(solicitacaoColeta).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a SolicitacaoColeta', () => {
      const solicitacaoColeta = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(solicitacaoColeta).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a SolicitacaoColeta', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of SolicitacaoColeta', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a SolicitacaoColeta', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    it('should handle exceptions for searching a SolicitacaoColeta', () => {
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

    describe('addSolicitacaoColetaToCollectionIfMissing', () => {
      it('should add a SolicitacaoColeta to an empty array', () => {
        const solicitacaoColeta: ISolicitacaoColeta = sampleWithRequiredData;
        expectedResult = service.addSolicitacaoColetaToCollectionIfMissing([], solicitacaoColeta);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(solicitacaoColeta);
      });

      it('should not add a SolicitacaoColeta to an array that contains it', () => {
        const solicitacaoColeta: ISolicitacaoColeta = sampleWithRequiredData;
        const solicitacaoColetaCollection: ISolicitacaoColeta[] = [
          {
            ...solicitacaoColeta,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addSolicitacaoColetaToCollectionIfMissing(solicitacaoColetaCollection, solicitacaoColeta);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a SolicitacaoColeta to an array that doesn't contain it", () => {
        const solicitacaoColeta: ISolicitacaoColeta = sampleWithRequiredData;
        const solicitacaoColetaCollection: ISolicitacaoColeta[] = [sampleWithPartialData];
        expectedResult = service.addSolicitacaoColetaToCollectionIfMissing(solicitacaoColetaCollection, solicitacaoColeta);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(solicitacaoColeta);
      });

      it('should add only unique SolicitacaoColeta to an array', () => {
        const solicitacaoColetaArray: ISolicitacaoColeta[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const solicitacaoColetaCollection: ISolicitacaoColeta[] = [sampleWithRequiredData];
        expectedResult = service.addSolicitacaoColetaToCollectionIfMissing(solicitacaoColetaCollection, ...solicitacaoColetaArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const solicitacaoColeta: ISolicitacaoColeta = sampleWithRequiredData;
        const solicitacaoColeta2: ISolicitacaoColeta = sampleWithPartialData;
        expectedResult = service.addSolicitacaoColetaToCollectionIfMissing([], solicitacaoColeta, solicitacaoColeta2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(solicitacaoColeta);
        expect(expectedResult).toContain(solicitacaoColeta2);
      });

      it('should accept null and undefined values', () => {
        const solicitacaoColeta: ISolicitacaoColeta = sampleWithRequiredData;
        expectedResult = service.addSolicitacaoColetaToCollectionIfMissing([], null, solicitacaoColeta, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(solicitacaoColeta);
      });

      it('should return initial array if no SolicitacaoColeta is added', () => {
        const solicitacaoColetaCollection: ISolicitacaoColeta[] = [sampleWithRequiredData];
        expectedResult = service.addSolicitacaoColetaToCollectionIfMissing(solicitacaoColetaCollection, undefined, null);
        expect(expectedResult).toEqual(solicitacaoColetaCollection);
      });
    });

    describe('compareSolicitacaoColeta', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareSolicitacaoColeta(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareSolicitacaoColeta(entity1, entity2);
        const compareResult2 = service.compareSolicitacaoColeta(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareSolicitacaoColeta(entity1, entity2);
        const compareResult2 = service.compareSolicitacaoColeta(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareSolicitacaoColeta(entity1, entity2);
        const compareResult2 = service.compareSolicitacaoColeta(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
