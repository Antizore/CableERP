package com.example.CableERP.MRP;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/mrp/run")
public class MRPController {

    private final Service service;

    public MRPController(Service service){
        this.service = service;
    }


    @PostMapping
    public ResponseEntity<MRPResponseDTO> mrpRun(){

        MRPResponseDTO responseDTO = new MRPResponseDTO(service.mrpRun());
        return ResponseEntity
                .ok()
                .body(responseDTO);


    }


}
