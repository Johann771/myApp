import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { QualityrecordsService } from '../service/qualityrecords.service';
import { IQualityrecords } from '../qualityrecords.model';
import { QualityrecordsFormService } from './qualityrecords-form.service';

import { QualityrecordsUpdateComponent } from './qualityrecords-update.component';

describe('Qualityrecords Management Update Component', () => {
  let comp: QualityrecordsUpdateComponent;
  let fixture: ComponentFixture<QualityrecordsUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let qualityrecordsFormService: QualityrecordsFormService;
  let qualityrecordsService: QualityrecordsService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([]), QualityrecordsUpdateComponent],
      providers: [
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(QualityrecordsUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(QualityrecordsUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    qualityrecordsFormService = TestBed.inject(QualityrecordsFormService);
    qualityrecordsService = TestBed.inject(QualityrecordsService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const qualityrecords: IQualityrecords = { id: 456 };

      activatedRoute.data = of({ qualityrecords });
      comp.ngOnInit();

      expect(comp.qualityrecords).toEqual(qualityrecords);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IQualityrecords>>();
      const qualityrecords = { id: 123 };
      jest.spyOn(qualityrecordsFormService, 'getQualityrecords').mockReturnValue(qualityrecords);
      jest.spyOn(qualityrecordsService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ qualityrecords });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: qualityrecords }));
      saveSubject.complete();

      // THEN
      expect(qualityrecordsFormService.getQualityrecords).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(qualityrecordsService.update).toHaveBeenCalledWith(expect.objectContaining(qualityrecords));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IQualityrecords>>();
      const qualityrecords = { id: 123 };
      jest.spyOn(qualityrecordsFormService, 'getQualityrecords').mockReturnValue({ id: null });
      jest.spyOn(qualityrecordsService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ qualityrecords: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: qualityrecords }));
      saveSubject.complete();

      // THEN
      expect(qualityrecordsFormService.getQualityrecords).toHaveBeenCalled();
      expect(qualityrecordsService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IQualityrecords>>();
      const qualityrecords = { id: 123 };
      jest.spyOn(qualityrecordsService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ qualityrecords });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(qualityrecordsService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
