import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ItemTileComponent } from './item-tile.component';

describe('ItemTileComponent', () => {
  let component: ItemTileComponent;
  let fixture: ComponentFixture<ItemTileComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ItemTileComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(ItemTileComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
