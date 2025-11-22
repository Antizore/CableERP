package com.example.CableERP.MRP;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/mrp/run")
public class Controller {

    private final Service service;

    public Controller(Service service){
        this.service = service;
    }


    @PostMapping
    public void mrpRun(){
        service.mrpRun();
    }


}
