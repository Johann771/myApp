import { TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness, RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { QualityrecordsDetailComponent } from './qualityrecords-detail.component';

describe('Qualityrecords Management Detail Component', () => {
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [QualityrecordsDetailComponent, RouterTestingModule.withRoutes([], { bindToComponentInputs: true })],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              component: QualityrecordsDetailComponent,
              resolve: { qualityrecords: () => of({ id: 123 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(QualityrecordsDetailComponent, '')
      .compileComponents();
  });

  describe('OnInit', () => {
    it('Should load qualityrecords on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', QualityrecordsDetailComponent);

      // THEN
      expect(instance.qualityrecords).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
