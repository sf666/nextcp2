import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SongOptionsComponent } from './song-options.component';

describe('SongOptionsComponent', () => {
  let component: SongOptionsComponent;
  let fixture: ComponentFixture<SongOptionsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
    imports: [SongOptionsComponent]
})
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(SongOptionsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
