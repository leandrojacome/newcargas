import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { ITransportadora } from '../transportadora.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../transportadora.test-samples';

import { TransportadoraService, RestTransportadora } from './transportadora.service';

const requireRestSample: RestTransportadora = {
  ...sampleWithRequiredData,
  dataCadastro: sampleWithRequiredData.dataCadastro?.toJSON(),
  dataAtualizacao: sampleWithRequiredData.dataAtualizacao?.toJSON(),
};

describe('Transportadora Service', () => {
  let service: TransportadoraService;
  let httpMock: HttpTestingController;
  let expectedResult: ITransportadora | ITransportadora[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(TransportadoraService);
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

    it('should create a Transportadora', () => {
      const transportadora = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(transportadora).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Transportadora', () => {
      const transportadora = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(transportadora).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Transportadora', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Transportadora', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a Transportadora', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    it('should handle exceptions for searching a Transportadora', () => {
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

    describe('addTransportadoraToCollectionIfMissing', () => {
      it('should add a Transportadora to an empty array', () => {
        const transportadora: ITransportadora = sampleWithRequiredData;
        expectedResult = service.addTransportadoraToCollectionIfMissing([], transportadora);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(transportadora);
      });

      it('should not add a Transportadora to an array that contains it', () => {
        const transportadora: ITransportadora = sampleWithRequiredData;
        const transportadoraCollection: ITransportadora[] = [
          {
            ...transportadora,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addTransportadoraToCollectionIfMissing(transportadoraCollection, transportadora);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Transportadora to an array that doesn't contain it", () => {
        const transportadora: ITransportadora = sampleWithRequiredData;
        const transportadoraCollection: ITransportadora[] = [sampleWithPartialData];
        expectedResult = service.addTransportadoraToCollectionIfMissing(transportadoraCollection, transportadora);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(transportadora);
      });

      it('should add only unique Transportadora to an array', () => {
        const transportadoraArray: ITransportadora[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const transportadoraCollection: ITransportadora[] = [sampleWithRequiredData];
        expectedResult = service.addTransportadoraToCollectionIfMissing(transportadoraCollection, ...transportadoraArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const transportadora: ITransportadora = sampleWithRequiredData;
        const transportadora2: ITransportadora = sampleWithPartialData;
        expectedResult = service.addTransportadoraToCollectionIfMissing([], transportadora, transportadora2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(transportadora);
        expect(expectedResult).toContain(transportadora2);
      });

      it('should accept null and undefined values', () => {
        const transportadora: ITransportadora = sampleWithRequiredData;
        expectedResult = service.addTransportadoraToCollectionIfMissing([], null, transportadora, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(transportadora);
      });

      it('should return initial array if no Transportadora is added', () => {
        const transportadoraCollection: ITransportadora[] = [sampleWithRequiredData];
        expectedResult = service.addTransportadoraToCollectionIfMissing(transportadoraCollection, undefined, null);
        expect(expectedResult).toEqual(transportadoraCollection);
      });
    });

    describe('compareTransportadora', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareTransportadora(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareTransportadora(entity1, entity2);
        const compareResult2 = service.compareTransportadora(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareTransportadora(entity1, entity2);
        const compareResult2 = service.compareTransportadora(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareTransportadora(entity1, entity2);
        const compareResult2 = service.compareTransportadora(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
