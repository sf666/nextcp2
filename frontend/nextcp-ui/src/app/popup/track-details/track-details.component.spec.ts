import { ComponentFixture, TestBed } from '@angular/core/testing';

import { TrackDetailsComponent } from './track-details.component';

describe('TrackDetailsComponent', () => {
  let component: TrackDetailsComponent;
  let fixture: ComponentFixture<TrackDetailsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
    imports: [TrackDetailsComponent]
})
    .compileComponents();

    fixture = TestBed.createComponent(TrackDetailsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
