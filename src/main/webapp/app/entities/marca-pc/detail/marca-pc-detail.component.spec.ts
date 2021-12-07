import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { MarcaPcDetailComponent } from './marca-pc-detail.component';

describe('MarcaPc Management Detail Component', () => {
  let comp: MarcaPcDetailComponent;
  let fixture: ComponentFixture<MarcaPcDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [MarcaPcDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ marcaPc: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(MarcaPcDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(MarcaPcDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load marcaPc on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.marcaPc).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
