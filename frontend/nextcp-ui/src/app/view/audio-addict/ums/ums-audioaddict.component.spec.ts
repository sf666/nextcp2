import { ComponentFixture, TestBed } from '@angular/core/testing';

import { UmsAudioaddictComponent } from './ums-audioaddict.component';

describe('UmsComponent', () => {
  let component: UmsAudioaddictComponent;
  let fixture: ComponentFixture<UmsAudioaddictComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [UmsAudioaddictComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(UmsAudioaddictComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
