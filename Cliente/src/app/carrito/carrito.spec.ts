import { ComponentFixture, TestBed } from '@angular/core/testing';
import { Carrito } from './carrito';
import { CarritoService } from '../services/carrito-service';
import { of } from 'rxjs';
import { Router } from '@angular/router';

class MockCarritoService {
  getCart() {
    return of([]);
  }
  calculateTotal() {
    return 0;
  }
  itemCount() {
    return 0;
  }
}

class MockRouter {
    navigate = jasmine.createSpy('navigate');
}

describe('Carrito', () => {
  let component: Carrito;
  let fixture: ComponentFixture<Carrito>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [Carrito],
      providers: [
        { provide: CarritoService, useClass: MockCarritoService },
        { provide: Router, useClass: MockRouter }
      ]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(Carrito);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
