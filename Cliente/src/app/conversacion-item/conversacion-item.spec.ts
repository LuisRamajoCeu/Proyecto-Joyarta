import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ConversacionItem } from './conversacion-item.component';

describe('ConversacionItem', () => {
  let component: ConversacionItem;
  let fixture: ComponentFixture<ConversacionItem>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ConversacionItem]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(ConversacionItem);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
