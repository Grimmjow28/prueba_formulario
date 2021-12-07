import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import * as dayjs from 'dayjs';

import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IRespuesta, Respuesta } from '../respuesta.model';

import { RespuestaService } from './respuesta.service';

describe('Respuesta Service', () => {
  let service: RespuestaService;
  let httpMock: HttpTestingController;
  let elemDefault: IRespuesta;
  let expectedResult: IRespuesta | IRespuesta[] | boolean | null;
  let currentDate: dayjs.Dayjs;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(RespuestaService);
    httpMock = TestBed.inject(HttpTestingController);
    currentDate = dayjs();

    elemDefault = {
      id: 0,
      numeroIdentificacion: 0,
      email: 'AAAAAAA',
      comentarios: 'AAAAAAA',
      fechaHora: currentDate,
    };
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = Object.assign(
        {
          fechaHora: currentDate.format(DATE_TIME_FORMAT),
        },
        elemDefault
      );

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(elemDefault);
    });

    it('should create a Respuesta', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
          fechaHora: currentDate.format(DATE_TIME_FORMAT),
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          fechaHora: currentDate,
        },
        returnedFromService
      );

      service.create(new Respuesta()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Respuesta', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          numeroIdentificacion: 1,
          email: 'BBBBBB',
          comentarios: 'BBBBBB',
          fechaHora: currentDate.format(DATE_TIME_FORMAT),
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          fechaHora: currentDate,
        },
        returnedFromService
      );

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Respuesta', () => {
      const patchObject = Object.assign(
        {
          email: 'BBBBBB',
          comentarios: 'BBBBBB',
          fechaHora: currentDate.format(DATE_TIME_FORMAT),
        },
        new Respuesta()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign(
        {
          fechaHora: currentDate,
        },
        returnedFromService
      );

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Respuesta', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          numeroIdentificacion: 1,
          email: 'BBBBBB',
          comentarios: 'BBBBBB',
          fechaHora: currentDate.format(DATE_TIME_FORMAT),
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          fechaHora: currentDate,
        },
        returnedFromService
      );

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toContainEqual(expected);
    });

    it('should delete a Respuesta', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addRespuestaToCollectionIfMissing', () => {
      it('should add a Respuesta to an empty array', () => {
        const respuesta: IRespuesta = { id: 123 };
        expectedResult = service.addRespuestaToCollectionIfMissing([], respuesta);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(respuesta);
      });

      it('should not add a Respuesta to an array that contains it', () => {
        const respuesta: IRespuesta = { id: 123 };
        const respuestaCollection: IRespuesta[] = [
          {
            ...respuesta,
          },
          { id: 456 },
        ];
        expectedResult = service.addRespuestaToCollectionIfMissing(respuestaCollection, respuesta);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Respuesta to an array that doesn't contain it", () => {
        const respuesta: IRespuesta = { id: 123 };
        const respuestaCollection: IRespuesta[] = [{ id: 456 }];
        expectedResult = service.addRespuestaToCollectionIfMissing(respuestaCollection, respuesta);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(respuesta);
      });

      it('should add only unique Respuesta to an array', () => {
        const respuestaArray: IRespuesta[] = [{ id: 123 }, { id: 456 }, { id: 40226 }];
        const respuestaCollection: IRespuesta[] = [{ id: 123 }];
        expectedResult = service.addRespuestaToCollectionIfMissing(respuestaCollection, ...respuestaArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const respuesta: IRespuesta = { id: 123 };
        const respuesta2: IRespuesta = { id: 456 };
        expectedResult = service.addRespuestaToCollectionIfMissing([], respuesta, respuesta2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(respuesta);
        expect(expectedResult).toContain(respuesta2);
      });

      it('should accept null and undefined values', () => {
        const respuesta: IRespuesta = { id: 123 };
        expectedResult = service.addRespuestaToCollectionIfMissing([], null, respuesta, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(respuesta);
      });

      it('should return initial array if no Respuesta is added', () => {
        const respuestaCollection: IRespuesta[] = [{ id: 123 }];
        expectedResult = service.addRespuestaToCollectionIfMissing(respuestaCollection, undefined, null);
        expect(expectedResult).toEqual(respuestaCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
