import { ComponentFixture, TestBed } from '@angular/core/testing';

import { MusicLibrary2Component } from './music-library2.component';

describe('MusicLibrary2Component', () => {
  let component: MusicLibrary2Component;
  let fixture: ComponentFixture<MusicLibrary2Component>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [MusicLibrary2Component]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(MusicLibrary2Component);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
