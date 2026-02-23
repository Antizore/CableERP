package simpleerp.customer;


import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/v1/customers")

public class CustomerController {

    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @PostMapping
    public ResponseEntity<Customer> createCustomer(@Valid @RequestBody CreateCustomerDTO customer){
        Customer createdCustomer = customerService.createCustomer(customer);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(createdCustomer.getId())
                .toUri();

        return ResponseEntity
                .created(location)
                .body(createdCustomer);
    }


    @GetMapping
    public ResponseEntity<List<Customer>> getCustomers(){
        return ResponseEntity
                .ok()
                .body(customerService.getListOfCustomers());
    }


}
