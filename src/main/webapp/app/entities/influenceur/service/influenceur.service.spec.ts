import { afterEach, beforeEach, describe, expect, it } from 'vitest';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';

import { IInfluenceur } from '../influenceur.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../influenceur.test-samples';

import { InfluenceurService } from './influenceur.service';

const requireRestSample: IInfluenceur = {
  ...sampleWithRequiredData,
};

describe('Influenceur Service', () => {
  let service: InfluenceurService;
  let httpMock: HttpTestingController;
  let expectedResult: IInfluenceur | IInfluenceur[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(InfluenceurService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.find(123).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should create a Influenceur', () => {
      const influenceur = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(influenceur).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Influenceur', () => {
      const influenceur = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(influenceur).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Influenceur', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Influenceur', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a Influenceur', () => {
      service.delete(123).subscribe();

      const requests = httpMock.match({ method: 'DELETE' });
      expect(requests.length).toBe(1);
    });

    describe('addInfluenceurToCollectionIfMissing', () => {
      it('should add a Influenceur to an empty array', () => {
        const influenceur: IInfluenceur = sampleWithRequiredData;
        expectedResult = service.addInfluenceurToCollectionIfMissing([], influenceur);
        expect(expectedResult).toEqual([influenceur]);
      });

      it('should not add a Influenceur to an array that contains it', () => {
        const influenceur: IInfluenceur = sampleWithRequiredData;
        const influenceurCollection: IInfluenceur[] = [
          {
            ...influenceur,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addInfluenceurToCollectionIfMissing(influenceurCollection, influenceur);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Influenceur to an array that doesn't contain it", () => {
        const influenceur: IInfluenceur = sampleWithRequiredData;
        const influenceurCollection: IInfluenceur[] = [sampleWithPartialData];
        expectedResult = service.addInfluenceurToCollectionIfMissing(influenceurCollection, influenceur);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(influenceur);
      });

      it('should add only unique Influenceur to an array', () => {
        const influenceurArray: IInfluenceur[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const influenceurCollection: IInfluenceur[] = [sampleWithRequiredData];
        expectedResult = service.addInfluenceurToCollectionIfMissing(influenceurCollection, ...influenceurArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const influenceur: IInfluenceur = sampleWithRequiredData;
        const influenceur2: IInfluenceur = sampleWithPartialData;
        expectedResult = service.addInfluenceurToCollectionIfMissing([], influenceur, influenceur2);
        expect(expectedResult).toEqual([influenceur, influenceur2]);
      });

      it('should accept null and undefined values', () => {
        const influenceur: IInfluenceur = sampleWithRequiredData;
        expectedResult = service.addInfluenceurToCollectionIfMissing([], null, influenceur, undefined);
        expect(expectedResult).toEqual([influenceur]);
      });

      it('should return initial array if no Influenceur is added', () => {
        const influenceurCollection: IInfluenceur[] = [sampleWithRequiredData];
        expectedResult = service.addInfluenceurToCollectionIfMissing(influenceurCollection, undefined, null);
        expect(expectedResult).toEqual(influenceurCollection);
      });
    });

    describe('compareInfluenceur', () => {
      it('should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareInfluenceur(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('should return false if one entity is null', () => {
        const entity1 = { id: 16800 };
        const entity2 = null;

        const compareResult1 = service.compareInfluenceur(entity1, entity2);
        const compareResult2 = service.compareInfluenceur(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey differs', () => {
        const entity1 = { id: 16800 };
        const entity2 = { id: 28452 };

        const compareResult1 = service.compareInfluenceur(entity1, entity2);
        const compareResult2 = service.compareInfluenceur(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey matches', () => {
        const entity1 = { id: 16800 };
        const entity2 = { id: 16800 };

        const compareResult1 = service.compareInfluenceur(entity1, entity2);
        const compareResult2 = service.compareInfluenceur(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
