import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { MusicLibraryComponent } from './music-library.component';

describe('MusicLibraryComponent', () => {
  let component: MusicLibraryComponent;
  let fixture: ComponentFixture<MusicLibraryComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ MusicLibraryComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(MusicLibraryComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
