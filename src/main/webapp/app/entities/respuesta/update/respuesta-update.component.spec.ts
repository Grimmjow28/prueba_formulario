jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { RespuestaService } from '../service/respuesta.service';
import { IRespuesta, Respuesta } from '../respuesta.model';
import { IMarcaPc } from 'app/entities/marca-pc/marca-pc.model';
import { MarcaPcService } from 'app/entities/marca-pc/service/marca-pc.service';

import { RespuestaUpdateComponent } from './respuesta-update.component';

describe('Respuesta Management Update Component', () => {
  let comp: RespuestaUpdateComponent;
  let fixture: ComponentFixture<RespuestaUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let respuestaService: RespuestaService;
  let marcaPcService: MarcaPcService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [RespuestaUpdateComponent],
      providers: [FormBuilder, ActivatedRoute],
    })
      .overrideTemplate(RespuestaUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(RespuestaUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    respuestaService = TestBed.inject(RespuestaService);
    marcaPcService = TestBed.inject(MarcaPcService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call MarcaPc query and add missing value', () => {
      const respuesta: IRespuesta = { id: 456 };
      const marcaPc: IMarcaPc = { id: 19253 };
      respuesta.marcaPc = marcaPc;

      const marcaPcCollection: IMarcaPc[] = [{ id: 97054 }];
      jest.spyOn(marcaPcService, 'query').mockReturnValue(of(new HttpResponse({ body: marcaPcCollection })));
      const additionalMarcaPcs = [marcaPc];
      const expectedCollection: IMarcaPc[] = [...additionalMarcaPcs, ...marcaPcCollection];
      jest.spyOn(marcaPcService, 'addMarcaPcToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ respuesta });
      comp.ngOnInit();

      expect(marcaPcService.query).toHaveBeenCalled();
      expect(marcaPcService.addMarcaPcToCollectionIfMissing).toHaveBeenCalledWith(marcaPcCollection, ...additionalMarcaPcs);
      expect(comp.marcaPcsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const respuesta: IRespuesta = { id: 456 };
      const marcaPc: IMarcaPc = { id: 6914 };
      respuesta.marcaPc = marcaPc;

      activatedRoute.data = of({ respuesta });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(respuesta));
      expect(comp.marcaPcsSharedCollection).toContain(marcaPc);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Respuesta>>();
      const respuesta = { id: 123 };
      jest.spyOn(respuestaService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ respuesta });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: respuesta }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(respuestaService.update).toHaveBeenCalledWith(respuesta);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Respuesta>>();
      const respuesta = new Respuesta();
      jest.spyOn(respuestaService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ respuesta });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: respuesta }));
      saveSubject.complete();

      // THEN
      expect(respuestaService.create).toHaveBeenCalledWith(respuesta);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Respuesta>>();
      const respuesta = { id: 123 };
      jest.spyOn(respuestaService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ respuesta });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(respuestaService.update).toHaveBeenCalledWith(respuesta);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Tracking relationships identifiers', () => {
    describe('trackMarcaPcById', () => {
      it('Should return tracked MarcaPc primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackMarcaPcById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });
  });
});
