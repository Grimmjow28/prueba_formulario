import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IMarcaPc, MarcaPc } from '../marca-pc.model';
import { MarcaPcService } from '../service/marca-pc.service';

@Injectable({ providedIn: 'root' })
export class MarcaPcRoutingResolveService implements Resolve<IMarcaPc> {
  constructor(protected service: MarcaPcService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IMarcaPc> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((marcaPc: HttpResponse<MarcaPc>) => {
          if (marcaPc.body) {
            return of(marcaPc.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new MarcaPc());
  }
}
