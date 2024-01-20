import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { ITabelaFrete } from '../tabela-frete.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../tabela-frete.test-samples';

import { TabelaFreteService, RestTabelaFrete } from './tabela-frete.service';

const requireRestSample: RestTabelaFrete = {
  ...sampleWithRequiredData,
  dataCadastro: sampleWithRequiredData.dataCadastro?.toJSON(),
  dataAtualizacao: sampleWithRequiredData.dataAtualizacao?.toJSON(),
};

describe('TabelaFrete Service', () => {
  let service: TabelaFreteService;
  let httpMock: HttpTestingController;
  let expectedResult: ITabelaFrete | ITabelaFrete[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(TabelaFreteService);
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

    it('should create a TabelaFrete', () => {
      const tabelaFrete = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(tabelaFrete).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a TabelaFrete', () => {
      const tabelaFrete = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(tabelaFrete).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a TabelaFrete', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of TabelaFrete', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a TabelaFrete', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    it('should handle exceptions for searching a TabelaFrete', () => {
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

    describe('addTabelaFreteToCollectionIfMissing', () => {
      it('should add a TabelaFrete to an empty array', () => {
        const tabelaFrete: ITabelaFrete = sampleWithRequiredData;
        expectedResult = service.addTabelaFreteToCollectionIfMissing([], tabelaFrete);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(tabelaFrete);
      });

      it('should not add a TabelaFrete to an array that contains it', () => {
        const tabelaFrete: ITabelaFrete = sampleWithRequiredData;
        const tabelaFreteCollection: ITabelaFrete[] = [
          {
            ...tabelaFrete,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addTabelaFreteToCollectionIfMissing(tabelaFreteCollection, tabelaFrete);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a TabelaFrete to an array that doesn't contain it", () => {
        const tabelaFrete: ITabelaFrete = sampleWithRequiredData;
        const tabelaFreteCollection: ITabelaFrete[] = [sampleWithPartialData];
        expectedResult = service.addTabelaFreteToCollectionIfMissing(tabelaFreteCollection, tabelaFrete);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(tabelaFrete);
      });

      it('should add only unique TabelaFrete to an array', () => {
        const tabelaFreteArray: ITabelaFrete[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const tabelaFreteCollection: ITabelaFrete[] = [sampleWithRequiredData];
        expectedResult = service.addTabelaFreteToCollectionIfMissing(tabelaFreteCollection, ...tabelaFreteArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const tabelaFrete: ITabelaFrete = sampleWithRequiredData;
        const tabelaFrete2: ITabelaFrete = sampleWithPartialData;
        expectedResult = service.addTabelaFreteToCollectionIfMissing([], tabelaFrete, tabelaFrete2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(tabelaFrete);
        expect(expectedResult).toContain(tabelaFrete2);
      });

      it('should accept null and undefined values', () => {
        const tabelaFrete: ITabelaFrete = sampleWithRequiredData;
        expectedResult = service.addTabelaFreteToCollectionIfMissing([], null, tabelaFrete, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(tabelaFrete);
      });

      it('should return initial array if no TabelaFrete is added', () => {
        const tabelaFreteCollection: ITabelaFrete[] = [sampleWithRequiredData];
        expectedResult = service.addTabelaFreteToCollectionIfMissing(tabelaFreteCollection, undefined, null);
        expect(expectedResult).toEqual(tabelaFreteCollection);
      });
    });

    describe('compareTabelaFrete', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareTabelaFrete(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareTabelaFrete(entity1, entity2);
        const compareResult2 = service.compareTabelaFrete(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareTabelaFrete(entity1, entity2);
        const compareResult2 = service.compareTabelaFrete(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareTabelaFrete(entity1, entity2);
        const compareResult2 = service.compareTabelaFrete(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
