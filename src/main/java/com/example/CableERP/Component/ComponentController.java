package com.example.CableERP.Component;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/components")
public class ComponentController {

    private final ComponentService componentService;

    public ComponentController(ComponentService componentService) {
        this.componentService = componentService;
    }


    @PostMapping
    public ResponseEntity<Component> addComponent(@RequestBody Component component) throws Exception {
        Component created = componentService.addComponent(component);
        URI location = URI.create("/components/"+created.getId());

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
