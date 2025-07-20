import {Component, OnInit} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {CustomerService} from '../services/customer-service';

@Component({
  selector: 'app-customers',
  imports: [],
  templateUrl: './customers.html',
  styleUrl: './customers.css'
})
export class Customers implements OnInit{
  customers : any;
  constructor(private customerService: CustomerService) {
  }

  ngOnInit(): void {
      this.customerService.getCustomers().subscribe({
        next:(data)=>{
          this.customers = data
        },
        error:(err) => {
          console.error(err);
        }
      })
    }


}
