import {Customer} from './customer';

export interface Account {
  id:        string;
  createdAt: Date;
  balance:   number;
  status:    string;
  currency:  string;
  customer:  Customer;
  type:      string;
  overDraft: number;
  interestRate: number;
}

