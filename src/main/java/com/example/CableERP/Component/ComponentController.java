package com.example.CableERP.Component;


import org.apache.coyote.Response;
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
    public ResponseEntity<Component> addComponent(@RequestBody ComponentCreateDTO component) throws Exception {
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

    @GetMapping("/{id}")
    public ResponseEntity<Component> getComponent(@RequestParam Long id){
        return ResponseEntity
                .ok()
                .body(componentService.getComponent(id));
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteComponent(@RequestParam Long id){
        return ResponseEntity
                .noContent().build();
    }


    @PatchMapping("/{id}")
    public ResponseEntity<Component> editComponent(@RequestParam Long id, @RequestBody ComponentCreateDTO component){
        return ResponseEntity
                .ok()
                .body(componentService.patchComponent(id, component));
    }


}
