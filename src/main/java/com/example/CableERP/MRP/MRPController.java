package com.example.CableERP.MRP;

import com.example.CableERP.Component.ComponentMRPDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/mrp/run")
public class MRPController {

    private final Service service;

    public MRPController(Service service){
        this.service = service;
    }


    @PostMapping
    public ResponseEntity<List<List<ComponentMRPDTO>>> mrpRun(){

        List<List<ComponentMRPDTO>> responseDTO = service.mrpRun().stream().toList();


        return ResponseEntity
                .ok()
                .body(responseDTO);


    }


}
