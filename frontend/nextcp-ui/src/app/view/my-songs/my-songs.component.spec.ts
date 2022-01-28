import { ComponentFixture, TestBed } from '@angular/core/testing';

import { MySongsComponent } from './my-songs.component';

describe('MySongsComponent', () => {
  let component: MySongsComponent;
  let fixture: ComponentFixture<MySongsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ MySongsComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(MySongsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
