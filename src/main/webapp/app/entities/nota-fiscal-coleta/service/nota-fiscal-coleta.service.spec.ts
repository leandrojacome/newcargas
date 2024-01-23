import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { INotaFiscalColeta } from '../nota-fiscal-coleta.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../nota-fiscal-coleta.test-samples';

import { NotaFiscalColetaService, RestNotaFiscalColeta } from './nota-fiscal-coleta.service';

const requireRestSample: RestNotaFiscalColeta = {
  ...sampleWithRequiredData,
  dataEmissao: sampleWithRequiredData.dataEmissao?.toJSON(),
  dataSaida: sampleWithRequiredData.dataSaida?.toJSON(),
  createdDate: sampleWithRequiredData.createdDate?.toJSON(),
  lastModifiedDate: sampleWithRequiredData.lastModifiedDate?.toJSON(),
};

describe('NotaFiscalColeta Service', () => {
  let service: NotaFiscalColetaService;
  let httpMock: HttpTestingController;
  let expectedResult: INotaFiscalColeta | INotaFiscalColeta[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(NotaFiscalColetaService);
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

    it('should create a NotaFiscalColeta', () => {
      const notaFiscalColeta = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(notaFiscalColeta).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a NotaFiscalColeta', () => {
      const notaFiscalColeta = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(notaFiscalColeta).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a NotaFiscalColeta', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of NotaFiscalColeta', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a NotaFiscalColeta', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    it('should handle exceptions for searching a NotaFiscalColeta', () => {
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

    describe('addNotaFiscalColetaToCollectionIfMissing', () => {
      it('should add a NotaFiscalColeta to an empty array', () => {
        const notaFiscalColeta: INotaFiscalColeta = sampleWithRequiredData;
        expectedResult = service.addNotaFiscalColetaToCollectionIfMissing([], notaFiscalColeta);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(notaFiscalColeta);
      });

      it('should not add a NotaFiscalColeta to an array that contains it', () => {
        const notaFiscalColeta: INotaFiscalColeta = sampleWithRequiredData;
        const notaFiscalColetaCollection: INotaFiscalColeta[] = [
          {
            ...notaFiscalColeta,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addNotaFiscalColetaToCollectionIfMissing(notaFiscalColetaCollection, notaFiscalColeta);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a NotaFiscalColeta to an array that doesn't contain it", () => {
        const notaFiscalColeta: INotaFiscalColeta = sampleWithRequiredData;
        const notaFiscalColetaCollection: INotaFiscalColeta[] = [sampleWithPartialData];
        expectedResult = service.addNotaFiscalColetaToCollectionIfMissing(notaFiscalColetaCollection, notaFiscalColeta);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(notaFiscalColeta);
      });

      it('should add only unique NotaFiscalColeta to an array', () => {
        const notaFiscalColetaArray: INotaFiscalColeta[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const notaFiscalColetaCollection: INotaFiscalColeta[] = [sampleWithRequiredData];
        expectedResult = service.addNotaFiscalColetaToCollectionIfMissing(notaFiscalColetaCollection, ...notaFiscalColetaArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const notaFiscalColeta: INotaFiscalColeta = sampleWithRequiredData;
        const notaFiscalColeta2: INotaFiscalColeta = sampleWithPartialData;
        expectedResult = service.addNotaFiscalColetaToCollectionIfMissing([], notaFiscalColeta, notaFiscalColeta2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(notaFiscalColeta);
        expect(expectedResult).toContain(notaFiscalColeta2);
      });

      it('should accept null and undefined values', () => {
        const notaFiscalColeta: INotaFiscalColeta = sampleWithRequiredData;
        expectedResult = service.addNotaFiscalColetaToCollectionIfMissing([], null, notaFiscalColeta, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(notaFiscalColeta);
      });

      it('should return initial array if no NotaFiscalColeta is added', () => {
        const notaFiscalColetaCollection: INotaFiscalColeta[] = [sampleWithRequiredData];
        expectedResult = service.addNotaFiscalColetaToCollectionIfMissing(notaFiscalColetaCollection, undefined, null);
        expect(expectedResult).toEqual(notaFiscalColetaCollection);
      });
    });

    describe('compareNotaFiscalColeta', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareNotaFiscalColeta(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareNotaFiscalColeta(entity1, entity2);
        const compareResult2 = service.compareNotaFiscalColeta(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareNotaFiscalColeta(entity1, entity2);
        const compareResult2 = service.compareNotaFiscalColeta(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareNotaFiscalColeta(entity1, entity2);
        const compareResult2 = service.compareNotaFiscalColeta(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
