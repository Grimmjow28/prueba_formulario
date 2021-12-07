import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IRespuesta, Respuesta } from '../respuesta.model';
import { RespuestaService } from '../service/respuesta.service';

@Injectable({ providedIn: 'root' })
export class RespuestaRoutingResolveService implements Resolve<IRespuesta> {
  constructor(protected service: RespuestaService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IRespuesta> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((respuesta: HttpResponse<Respuesta>) => {
          if (respuesta.body) {
            return of(respuesta.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Respuesta());
  }
}
