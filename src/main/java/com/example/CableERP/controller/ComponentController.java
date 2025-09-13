package com.example.CableERP.controller;


import com.example.CableERP.entity.Component;
import com.example.CableERP.repository.ComponentRepository;
import com.example.CableERP.service.ComponentService;
import lombok.NoArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/components")
@NoArgsConstructor
public class ComponentController {

    private ComponentService componentService;


    @PostMapping
    public ResponseEntity<Component> addComponent(@RequestBody Component component) throws Exception {
        Component created = componentService.addComponent(component);
        URI location = URI.create("/components/"+created.getName());

        return ResponseEntity
                .created(location)
                .body(created);
    }

    @GetMapping
    public ResponseEntity<List<Component>> getComponents(){
        return ResponseEntity
                .ok()
                .body(componentService.getComponents());
    }
}
