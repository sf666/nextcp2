import { ComponentFixture, TestBed, waitForAsync } from '@angular/core/testing';

import { MusicLibraryComponent } from './music-library.component';

describe('MusicLibraryComponent', () => {
  let component: MusicLibraryComponent;
  let fixture: ComponentFixture<MusicLibraryComponent>;

  beforeEach(waitForAsync(() => {
    TestBed.configureTestingModule({
    imports: [MusicLibraryComponent]
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
