import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ServerDropdownComponent } from './dropdown.component';

describe('DropdownComponent', () => {
  let component: ServerDropdownComponent;
  let fixture: ComponentFixture<ServerDropdownComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ServerDropdownComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ServerDropdownComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
