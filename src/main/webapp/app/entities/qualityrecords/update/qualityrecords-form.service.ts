import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IQualityrecords, NewQualityrecords } from '../qualityrecords.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IQualityrecords for edit and NewQualityrecordsFormGroupInput for create.
 */
type QualityrecordsFormGroupInput = IQualityrecords | PartialWithRequiredKeyOf<NewQualityrecords>;

type QualityrecordsFormDefaults = Pick<NewQualityrecords, 'id'>;

type QualityrecordsFormGroupContent = {
  id: FormControl<IQualityrecords['id'] | NewQualityrecords['id']>;
  supplier: FormControl<IQualityrecords['supplier']>;
  test2: FormControl<IQualityrecords['test2']>;
};

export type QualityrecordsFormGroup = FormGroup<QualityrecordsFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class QualityrecordsFormService {
  createQualityrecordsFormGroup(qualityrecords: QualityrecordsFormGroupInput = { id: null }): QualityrecordsFormGroup {
    const qualityrecordsRawValue = {
      ...this.getFormDefaults(),
      ...qualityrecords,
    };
    return new FormGroup<QualityrecordsFormGroupContent>({
      id: new FormControl(
        { value: qualityrecordsRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      supplier: new FormControl(qualityrecordsRawValue.supplier, {
        validators: [Validators.required],
      }),
      test2: new FormControl(qualityrecordsRawValue.test2),
    });
  }

  getQualityrecords(form: QualityrecordsFormGroup): IQualityrecords | NewQualityrecords {
    return form.getRawValue() as IQualityrecords | NewQualityrecords;
  }

  resetForm(form: QualityrecordsFormGroup, qualityrecords: QualityrecordsFormGroupInput): void {
    const qualityrecordsRawValue = { ...this.getFormDefaults(), ...qualityrecords };
    form.reset(
      {
        ...qualityrecordsRawValue,
        id: { value: qualityrecordsRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): QualityrecordsFormDefaults {
    return {
      id: null,
    };
  }
}
