import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IQualityrecords, NewQualityrecords } from '../qualityrecords.model';

export type PartialUpdateQualityrecords = Partial<IQualityrecords> & Pick<IQualityrecords, 'id'>;

export type EntityResponseType = HttpResponse<IQualityrecords>;
export type EntityArrayResponseType = HttpResponse<IQualityrecords[]>;

@Injectable({ providedIn: 'root' })
export class QualityrecordsService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/qualityrecords');

  constructor(
    protected http: HttpClient,
    protected applicationConfigService: ApplicationConfigService,
  ) {}

  create(qualityrecords: NewQualityrecords): Observable<EntityResponseType> {
    return this.http.post<IQualityrecords>(this.resourceUrl, qualityrecords, { observe: 'response' });
  }

  update(qualityrecords: IQualityrecords): Observable<EntityResponseType> {
    return this.http.put<IQualityrecords>(`${this.resourceUrl}/${this.getQualityrecordsIdentifier(qualityrecords)}`, qualityrecords, {
      observe: 'response',
    });
  }

  partialUpdate(qualityrecords: PartialUpdateQualityrecords): Observable<EntityResponseType> {
    return this.http.patch<IQualityrecords>(`${this.resourceUrl}/${this.getQualityrecordsIdentifier(qualityrecords)}`, qualityrecords, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IQualityrecords>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IQualityrecords[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getQualityrecordsIdentifier(qualityrecords: Pick<IQualityrecords, 'id'>): number {
    return qualityrecords.id;
  }

  compareQualityrecords(o1: Pick<IQualityrecords, 'id'> | null, o2: Pick<IQualityrecords, 'id'> | null): boolean {
    return o1 && o2 ? this.getQualityrecordsIdentifier(o1) === this.getQualityrecordsIdentifier(o2) : o1 === o2;
  }

  addQualityrecordsToCollectionIfMissing<Type extends Pick<IQualityrecords, 'id'>>(
    qualityrecordsCollection: Type[],
    ...qualityrecordsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const qualityrecords: Type[] = qualityrecordsToCheck.filter(isPresent);
    if (qualityrecords.length > 0) {
      const qualityrecordsCollectionIdentifiers = qualityrecordsCollection.map(
        qualityrecordsItem => this.getQualityrecordsIdentifier(qualityrecordsItem)!,
      );
      const qualityrecordsToAdd = qualityrecords.filter(qualityrecordsItem => {
        const qualityrecordsIdentifier = this.getQualityrecordsIdentifier(qualityrecordsItem);
        if (qualityrecordsCollectionIdentifiers.includes(qualityrecordsIdentifier)) {
          return false;
        }
        qualityrecordsCollectionIdentifiers.push(qualityrecordsIdentifier);
        return true;
      });
      return [...qualityrecordsToAdd, ...qualityrecordsCollection];
    }
    return qualityrecordsCollection;
  }
}
