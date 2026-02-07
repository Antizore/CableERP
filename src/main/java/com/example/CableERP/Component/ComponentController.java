package com.example.CableERP.Component;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/v1/components")
public class ComponentController {

    private final ComponentService componentService;

    public ComponentController(ComponentService componentService) {
        this.componentService = componentService;
    }


    @PostMapping
    public ResponseEntity<ComponentResponseDTO> addComponent(@Validated @RequestBody ComponentCreateDTO component){
        ComponentResponseDTO created = componentService.addComponent(component);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(created.id())
                .toUri();

        return ResponseEntity
                .created(location)
                .body(created);
    }

    @GetMapping
    public ResponseEntity<List<ComponentResponseDTO>> getComponents(){
        return ResponseEntity.ok(componentService.getComponents());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ComponentResponseDTO> getComponent(@PathVariable Long id){
        return ResponseEntity.ok(componentService.getComponent(id));
    }


    @PatchMapping("/{id}")
    public ResponseEntity<ComponentResponseDTO> updateComponent(
            @PathVariable Long id,
            @RequestBody ComponentUpdateDTO componentUpdateDTO){

        return ResponseEntity
                .ok(componentService.patchComponent(id, componentUpdateDTO));
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteComponent(@PathVariable Long id){
        componentService.deleteComponent(id);
        return ResponseEntity.noContent().build();
    }


}
