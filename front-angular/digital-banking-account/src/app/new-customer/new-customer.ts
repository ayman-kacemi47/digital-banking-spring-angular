import {Component, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, ReactiveFormsModule, Validators} from '@angular/forms';
import {Customer} from '../models/customer';
import {CustomerService} from '../services/customer-service';
import {catchError, throwError} from 'rxjs';

@Component({
  selector: 'app-new-customer',
  imports: [
    ReactiveFormsModule
  ],
  templateUrl: './new-customer.html',
  styleUrl: './new-customer.css'
})
export class NewCustomer implements OnInit{

  newCustomerFormGroup!: FormGroup;

  constructor(private fb:FormBuilder, private customerService :CustomerService) {
    this.newCustomerFormGroup=this.fb.group(
      {
        name: this.fb.control("",[Validators.required, Validators.minLength(2)]),
        email: this.fb.control("",[Validators.required, Validators.email])
      }
    );
  }

  ngOnInit(): void {

    }



  handleSaveCustomer() {
    let customer:Customer = this.newCustomerFormGroup.value;
    console.log("hande ", customer)
    this.customerService.saveCustomer(customer).subscribe({
      next:data =>{
        alert("Customer saved successfully !!");
        this.newCustomerFormGroup.reset();
      },
      error:err => {
        console.error(err);
      }
    }


    );
  }
}
