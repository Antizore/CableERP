package com.example.CableERP.service;

import com.example.CableERP.entity.Component;
import com.example.CableERP.exception.NoNameException;
import com.example.CableERP.exception.WrongValueException;
import com.example.CableERP.repository.ComponentRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service

public class ComponentService {

    private final ComponentRepository componentRepository;

    public ComponentService(ComponentRepository componentRepository) {
        this.componentRepository = componentRepository;
    }


    public Component addComponent(Component component) throws Exception{
        if (component.getName().isBlank()) throw new NoNameException("Component name cannot be blank");
        else if (component.getCostPerUnit() < 0) throw new WrongValueException("Cost of component cannot be less than 0");
        else return componentRepository.saveAndFlush(component);
    }


    public List<Component> getComponents(){
        return componentRepository.findAll();
    }


}
