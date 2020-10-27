import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { MediarendererComponent } from './mediarenderer.component';

describe('MediarendererComponent', () => {
  let component: MediarendererComponent;
  let fixture: ComponentFixture<MediarendererComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ MediarendererComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(MediarendererComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
