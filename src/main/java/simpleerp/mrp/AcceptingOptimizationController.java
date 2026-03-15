package simpleerp.mrp;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/optimization")
public class AcceptingOptimizationController {



    @PostMapping("accept/{id}")
    public ResponseEntity<Void> acceptSuggestion(@PathVariable Long requestId){

        return ResponseEntity
                .ok()
                .build();
    }



}
