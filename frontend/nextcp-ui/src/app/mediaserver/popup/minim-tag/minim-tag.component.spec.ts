import { ComponentFixture, TestBed } from '@angular/core/testing';

import { MinimTagComponent } from './minim-tag.component';

describe('MinimTagComponent', () => {
  let component: MinimTagComponent;
  let fixture: ComponentFixture<MinimTagComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ MinimTagComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(MinimTagComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
