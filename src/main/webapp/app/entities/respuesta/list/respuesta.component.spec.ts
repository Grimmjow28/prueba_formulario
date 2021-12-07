import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { RespuestaService } from '../service/respuesta.service';

import { RespuestaComponent } from './respuesta.component';

describe('Respuesta Management Component', () => {
  let comp: RespuestaComponent;
  let fixture: ComponentFixture<RespuestaComponent>;
  let service: RespuestaService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [RespuestaComponent],
    })
      .overrideTemplate(RespuestaComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(RespuestaComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(RespuestaService);

    const headers = new HttpHeaders();
    jest.spyOn(service, 'query').mockReturnValue(
      of(
        new HttpResponse({
          body: [{ id: 123 }],
          headers,
        })
      )
    );
  });

  it('Should call load all on init', () => {
    // WHEN
    comp.ngOnInit();

    // THEN
    expect(service.query).toHaveBeenCalled();
    expect(comp.respuestas?.[0]).toEqual(expect.objectContaining({ id: 123 }));
  });
});
