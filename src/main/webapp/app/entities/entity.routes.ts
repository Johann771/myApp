import { Routes } from '@angular/router';

const routes: Routes = [
  {
    path: 'qualityrecords',
    data: { pageTitle: 'myApp.qualityrecords.home.title' },
    loadChildren: () => import('./qualityrecords/qualityrecords.routes'),
  },
  /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
];

export default routes;
