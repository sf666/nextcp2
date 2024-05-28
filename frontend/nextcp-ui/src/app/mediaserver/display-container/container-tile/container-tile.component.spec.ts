import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ContainerTileComponent } from './container-tile.component';

describe('ContainerTileComponent', () => {
  let component: ContainerTileComponent;
  let fixture: ComponentFixture<ContainerTileComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ContainerTileComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(ContainerTileComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
