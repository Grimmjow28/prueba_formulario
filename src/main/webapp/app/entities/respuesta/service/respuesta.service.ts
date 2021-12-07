import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as dayjs from 'dayjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IRespuesta, getRespuestaIdentifier } from '../respuesta.model';

export type EntityResponseType = HttpResponse<IRespuesta>;
export type EntityArrayResponseType = HttpResponse<IRespuesta[]>;

@Injectable({ providedIn: 'root' })
export class RespuestaService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/respuestas');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(respuesta: IRespuesta): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(respuesta);
    return this.http
      .post<IRespuesta>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(respuesta: IRespuesta): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(respuesta);
    return this.http
      .put<IRespuesta>(`${this.resourceUrl}/${getRespuestaIdentifier(respuesta) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(respuesta: IRespuesta): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(respuesta);
    return this.http
      .patch<IRespuesta>(`${this.resourceUrl}/${getRespuestaIdentifier(respuesta) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IRespuesta>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IRespuesta[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addRespuestaToCollectionIfMissing(
    respuestaCollection: IRespuesta[],
    ...respuestasToCheck: (IRespuesta | null | undefined)[]
  ): IRespuesta[] {
    const respuestas: IRespuesta[] = respuestasToCheck.filter(isPresent);
    if (respuestas.length > 0) {
      const respuestaCollectionIdentifiers = respuestaCollection.map(respuestaItem => getRespuestaIdentifier(respuestaItem)!);
      const respuestasToAdd = respuestas.filter(respuestaItem => {
        const respuestaIdentifier = getRespuestaIdentifier(respuestaItem);
        if (respuestaIdentifier == null || respuestaCollectionIdentifiers.includes(respuestaIdentifier)) {
          return false;
        }
        respuestaCollectionIdentifiers.push(respuestaIdentifier);
        return true;
      });
      return [...respuestasToAdd, ...respuestaCollection];
    }
    return respuestaCollection;
  }

  protected convertDateFromClient(respuesta: IRespuesta): IRespuesta {
    return Object.assign({}, respuesta, {
      fechaHora: respuesta.fechaHora?.isValid() ? respuesta.fechaHora.toJSON() : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.fechaHora = res.body.fechaHora ? dayjs(res.body.fechaHora) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((respuesta: IRespuesta) => {
        respuesta.fechaHora = respuesta.fechaHora ? dayjs(respuesta.fechaHora) : undefined;
      });
    }
    return res;
  }
}
