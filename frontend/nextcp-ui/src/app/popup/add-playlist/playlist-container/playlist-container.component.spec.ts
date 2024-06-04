import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PlaylistContainerComponent } from './playlist-container.component';

describe('PlaylistContainerComponent', () => {
  let component: PlaylistContainerComponent;
  let fixture: ComponentFixture<PlaylistContainerComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [PlaylistContainerComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(PlaylistContainerComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
