import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IMarcaPc } from '../marca-pc.model';

@Component({
  selector: 'jhi-marca-pc-detail',
  templateUrl: './marca-pc-detail.component.html',
})
export class MarcaPcDetailComponent implements OnInit {
  marcaPc: IMarcaPc | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ marcaPc }) => {
      this.marcaPc = marcaPc;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
