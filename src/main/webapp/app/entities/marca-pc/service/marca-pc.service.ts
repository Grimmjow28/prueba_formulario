import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IMarcaPc, getMarcaPcIdentifier } from '../marca-pc.model';

export type EntityResponseType = HttpResponse<IMarcaPc>;
export type EntityArrayResponseType = HttpResponse<IMarcaPc[]>;

@Injectable({ providedIn: 'root' })
export class MarcaPcService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/marca-pcs');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(marcaPc: IMarcaPc): Observable<EntityResponseType> {
    return this.http.post<IMarcaPc>(this.resourceUrl, marcaPc, { observe: 'response' });
  }

  update(marcaPc: IMarcaPc): Observable<EntityResponseType> {
    return this.http.put<IMarcaPc>(`${this.resourceUrl}/${getMarcaPcIdentifier(marcaPc) as number}`, marcaPc, { observe: 'response' });
  }

  partialUpdate(marcaPc: IMarcaPc): Observable<EntityResponseType> {
    return this.http.patch<IMarcaPc>(`${this.resourceUrl}/${getMarcaPcIdentifier(marcaPc) as number}`, marcaPc, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IMarcaPc>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IMarcaPc[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addMarcaPcToCollectionIfMissing(marcaPcCollection: IMarcaPc[], ...marcaPcsToCheck: (IMarcaPc | null | undefined)[]): IMarcaPc[] {
    const marcaPcs: IMarcaPc[] = marcaPcsToCheck.filter(isPresent);
    if (marcaPcs.length > 0) {
      const marcaPcCollectionIdentifiers = marcaPcCollection.map(marcaPcItem => getMarcaPcIdentifier(marcaPcItem)!);
      const marcaPcsToAdd = marcaPcs.filter(marcaPcItem => {
        const marcaPcIdentifier = getMarcaPcIdentifier(marcaPcItem);
        if (marcaPcIdentifier == null || marcaPcCollectionIdentifiers.includes(marcaPcIdentifier)) {
          return false;
        }
        marcaPcCollectionIdentifiers.push(marcaPcIdentifier);
        return true;
      });
      return [...marcaPcsToAdd, ...marcaPcCollection];
    }
    return marcaPcCollection;
  }
}
