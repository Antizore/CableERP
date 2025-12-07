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

    @GetMapping("/{id}")
    public ResponseEntity<Component> getComponent(@RequestParam Long id){
        return ResponseEntity
                .ok()
                .body(componentService.getComponent(id));
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteComponent(@RequestParam Long id){
        return ResponseEntity
                .ok()
                .body(componentService.deleteComponent(id));
    }


    @PatchMapping
    public ResponseEntity<Component> editComponent(@RequestParam Long id, @RequestBody Component component){
        return ResponseEntity
                .ok()
                .body(componentService.patchComponent(id, component));
    }


}
