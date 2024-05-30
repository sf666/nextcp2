import { ComponentFixture, TestBed } from '@angular/core/testing';

import { OtherItemTileComponent } from './other-item-tile.component';

describe('OtherItemTileComponent', () => {
  let component: OtherItemTileComponent;
  let fixture: ComponentFixture<OtherItemTileComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [OtherItemTileComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(OtherItemTileComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
