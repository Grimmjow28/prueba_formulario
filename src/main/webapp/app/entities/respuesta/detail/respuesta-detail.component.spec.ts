import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { RespuestaDetailComponent } from './respuesta-detail.component';

describe('Respuesta Management Detail Component', () => {
  let comp: RespuestaDetailComponent;
  let fixture: ComponentFixture<RespuestaDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [RespuestaDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ respuesta: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(RespuestaDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(RespuestaDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load respuesta on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.respuesta).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
