jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { MarcaPcService } from '../service/marca-pc.service';
import { IMarcaPc, MarcaPc } from '../marca-pc.model';

import { MarcaPcUpdateComponent } from './marca-pc-update.component';

describe('MarcaPc Management Update Component', () => {
  let comp: MarcaPcUpdateComponent;
  let fixture: ComponentFixture<MarcaPcUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let marcaPcService: MarcaPcService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [MarcaPcUpdateComponent],
      providers: [FormBuilder, ActivatedRoute],
    })
      .overrideTemplate(MarcaPcUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(MarcaPcUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    marcaPcService = TestBed.inject(MarcaPcService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const marcaPc: IMarcaPc = { id: 456 };

      activatedRoute.data = of({ marcaPc });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(marcaPc));
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<MarcaPc>>();
      const marcaPc = { id: 123 };
      jest.spyOn(marcaPcService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ marcaPc });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: marcaPc }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(marcaPcService.update).toHaveBeenCalledWith(marcaPc);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<MarcaPc>>();
      const marcaPc = new MarcaPc();
      jest.spyOn(marcaPcService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ marcaPc });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: marcaPc }));
      saveSubject.complete();

      // THEN
      expect(marcaPcService.create).toHaveBeenCalledWith(marcaPc);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<MarcaPc>>();
      const marcaPc = { id: 123 };
      jest.spyOn(marcaPcService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ marcaPc });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(marcaPcService.update).toHaveBeenCalledWith(marcaPc);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
