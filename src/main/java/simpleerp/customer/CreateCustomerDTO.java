package simpleerp.customer;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CreateCustomerDTO(

        @NotNull(message = "Customer name is required")
        @Size(min = 2, message = "Customer name needs to have 2 or more characters")
        String name,

        @Size(min = 3, max = 17, message = "Phone number needs to have 3 digits or more but no more than 17 digits")
        String phone,

        @Email(message = "must be a valid email address")
        String email
) { }
