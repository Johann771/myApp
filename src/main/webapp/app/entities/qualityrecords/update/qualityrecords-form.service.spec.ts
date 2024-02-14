import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../qualityrecords.test-samples';

import { QualityrecordsFormService } from './qualityrecords-form.service';

describe('Qualityrecords Form Service', () => {
  let service: QualityrecordsFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(QualityrecordsFormService);
  });

  describe('Service methods', () => {
    describe('createQualityrecordsFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createQualityrecordsFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            supplier: expect.any(Object),
            test2: expect.any(Object),
          }),
        );
      });

      it('passing IQualityrecords should create a new form with FormGroup', () => {
        const formGroup = service.createQualityrecordsFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            supplier: expect.any(Object),
            test2: expect.any(Object),
          }),
        );
      });
    });

    describe('getQualityrecords', () => {
      it('should return NewQualityrecords for default Qualityrecords initial value', () => {
        const formGroup = service.createQualityrecordsFormGroup(sampleWithNewData);

        const qualityrecords = service.getQualityrecords(formGroup) as any;

        expect(qualityrecords).toMatchObject(sampleWithNewData);
      });

      it('should return NewQualityrecords for empty Qualityrecords initial value', () => {
        const formGroup = service.createQualityrecordsFormGroup();

        const qualityrecords = service.getQualityrecords(formGroup) as any;

        expect(qualityrecords).toMatchObject({});
      });

      it('should return IQualityrecords', () => {
        const formGroup = service.createQualityrecordsFormGroup(sampleWithRequiredData);

        const qualityrecords = service.getQualityrecords(formGroup) as any;

        expect(qualityrecords).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IQualityrecords should not enable id FormControl', () => {
        const formGroup = service.createQualityrecordsFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewQualityrecords should disable id FormControl', () => {
        const formGroup = service.createQualityrecordsFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
