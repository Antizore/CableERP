package com.example.CableERP.controller;


import com.example.CableERP.entity.Component;
import com.example.CableERP.repository.ComponentRepository;
import com.example.CableERP.service.ComponentService;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@Controller
@RequestMapping("/components")
@RequiredArgsConstructor
public class ComponentController {

    private final ComponentService componentService;


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
