export interface AccountDetails {
  id:            string;
  balance:       number;
  currentPage:   number;
  pageSize:      number;
  totalPages:    number;
  operationDTOS: AccountOperations[];
}

export interface AccountOperations {
  id:            number;
  date:          Date;
  amount:        number;
  description:   string;
  operationType: string;
}
