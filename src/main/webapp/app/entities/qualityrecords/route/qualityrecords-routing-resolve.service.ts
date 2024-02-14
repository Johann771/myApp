import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IQualityrecords } from '../qualityrecords.model';
import { QualityrecordsService } from '../service/qualityrecords.service';

export const qualityrecordsResolve = (route: ActivatedRouteSnapshot): Observable<null | IQualityrecords> => {
  const id = route.params['id'];
  if (id) {
    return inject(QualityrecordsService)
      .find(id)
      .pipe(
        mergeMap((qualityrecords: HttpResponse<IQualityrecords>) => {
          if (qualityrecords.body) {
            return of(qualityrecords.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        }),
      );
  }
  return of(null);
};

export default qualityrecordsResolve;
