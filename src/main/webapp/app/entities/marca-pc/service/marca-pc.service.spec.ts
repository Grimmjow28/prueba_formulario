import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IMarcaPc, MarcaPc } from '../marca-pc.model';

import { MarcaPcService } from './marca-pc.service';

describe('MarcaPc Service', () => {
  let service: MarcaPcService;
  let httpMock: HttpTestingController;
  let elemDefault: IMarcaPc;
  let expectedResult: IMarcaPc | IMarcaPc[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(MarcaPcService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      nombreMarca: 'AAAAAAA',
    };
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = Object.assign({}, elemDefault);

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(elemDefault);
    });

    it('should create a MarcaPc', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new MarcaPc()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a MarcaPc', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          nombreMarca: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a MarcaPc', () => {
      const patchObject = Object.assign({}, new MarcaPc());

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of MarcaPc', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          nombreMarca: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toContainEqual(expected);
    });

    it('should delete a MarcaPc', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addMarcaPcToCollectionIfMissing', () => {
      it('should add a MarcaPc to an empty array', () => {
        const marcaPc: IMarcaPc = { id: 123 };
        expectedResult = service.addMarcaPcToCollectionIfMissing([], marcaPc);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(marcaPc);
      });

      it('should not add a MarcaPc to an array that contains it', () => {
        const marcaPc: IMarcaPc = { id: 123 };
        const marcaPcCollection: IMarcaPc[] = [
          {
            ...marcaPc,
          },
          { id: 456 },
        ];
        expectedResult = service.addMarcaPcToCollectionIfMissing(marcaPcCollection, marcaPc);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a MarcaPc to an array that doesn't contain it", () => {
        const marcaPc: IMarcaPc = { id: 123 };
        const marcaPcCollection: IMarcaPc[] = [{ id: 456 }];
        expectedResult = service.addMarcaPcToCollectionIfMissing(marcaPcCollection, marcaPc);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(marcaPc);
      });

      it('should add only unique MarcaPc to an array', () => {
        const marcaPcArray: IMarcaPc[] = [{ id: 123 }, { id: 456 }, { id: 86549 }];
        const marcaPcCollection: IMarcaPc[] = [{ id: 123 }];
        expectedResult = service.addMarcaPcToCollectionIfMissing(marcaPcCollection, ...marcaPcArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const marcaPc: IMarcaPc = { id: 123 };
        const marcaPc2: IMarcaPc = { id: 456 };
        expectedResult = service.addMarcaPcToCollectionIfMissing([], marcaPc, marcaPc2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(marcaPc);
        expect(expectedResult).toContain(marcaPc2);
      });

      it('should accept null and undefined values', () => {
        const marcaPc: IMarcaPc = { id: 123 };
        expectedResult = service.addMarcaPcToCollectionIfMissing([], null, marcaPc, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(marcaPc);
      });

      it('should return initial array if no MarcaPc is added', () => {
        const marcaPcCollection: IMarcaPc[] = [{ id: 123 }];
        expectedResult = service.addMarcaPcToCollectionIfMissing(marcaPcCollection, undefined, null);
        expect(expectedResult).toEqual(marcaPcCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
