import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DefautPlaylistsComponent } from './defaut-playlists.component';

describe('DefautPlaylistsComponent', () => {
  let component: DefautPlaylistsComponent;
  let fixture: ComponentFixture<DefautPlaylistsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ DefautPlaylistsComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(DefautPlaylistsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
