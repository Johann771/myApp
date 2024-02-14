import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import { QualityrecordsComponent } from './list/qualityrecords.component';
import { QualityrecordsDetailComponent } from './detail/qualityrecords-detail.component';
import { QualityrecordsUpdateComponent } from './update/qualityrecords-update.component';
import QualityrecordsResolve from './route/qualityrecords-routing-resolve.service';

const qualityrecordsRoute: Routes = [
  {
    path: '',
    component: QualityrecordsComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: QualityrecordsDetailComponent,
    resolve: {
      qualityrecords: QualityrecordsResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: QualityrecordsUpdateComponent,
    resolve: {
      qualityrecords: QualityrecordsResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: QualityrecordsUpdateComponent,
    resolve: {
      qualityrecords: QualityrecordsResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default qualityrecordsRoute;
