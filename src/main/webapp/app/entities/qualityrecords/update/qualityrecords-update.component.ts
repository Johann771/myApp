import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IQualityrecords } from '../qualityrecords.model';
import { QualityrecordsService } from '../service/qualityrecords.service';
import { QualityrecordsFormService, QualityrecordsFormGroup } from './qualityrecords-form.service';

@Component({
  standalone: true,
  selector: 'jhi-qualityrecords-update',
  templateUrl: './qualityrecords-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class QualityrecordsUpdateComponent implements OnInit {
  isSaving = false;
  qualityrecords: IQualityrecords | null = null;

  editForm: QualityrecordsFormGroup = this.qualityrecordsFormService.createQualityrecordsFormGroup();

  constructor(
    protected qualityrecordsService: QualityrecordsService,
    protected qualityrecordsFormService: QualityrecordsFormService,
    protected activatedRoute: ActivatedRoute,
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ qualityrecords }) => {
      this.qualityrecords = qualityrecords;
      if (qualityrecords) {
        this.updateForm(qualityrecords);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const qualityrecords = this.qualityrecordsFormService.getQualityrecords(this.editForm);
    if (qualityrecords.id !== null) {
      this.subscribeToSaveResponse(this.qualityrecordsService.update(qualityrecords));
    } else {
      this.subscribeToSaveResponse(this.qualityrecordsService.create(qualityrecords));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IQualityrecords>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(qualityrecords: IQualityrecords): void {
    this.qualityrecords = qualityrecords;
    this.qualityrecordsFormService.resetForm(this.editForm, qualityrecords);
  }
}
