import {Component, OnInit} from '@angular/core';
import {HttpClient} from '@angular/common/http';

@Component({
  selector: 'app-customers',
  imports: [],
  templateUrl: './customers.html',
  styleUrl: './customers.css'
})
export class Customers implements OnInit{
  customers : any;
  constructor(private http:HttpClient) {
  }

  ngOnInit(): void {
      this.http.get("http://localhost:8080/customers").subscribe({
        next:(data)=>{
          this.customers = data
        },
        error:(err) => {
          console.error(err);
        }
      })
    }


}
