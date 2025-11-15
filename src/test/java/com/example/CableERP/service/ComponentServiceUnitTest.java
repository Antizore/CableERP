package com.example.CableERP.service;


import com.example.CableERP.Components.Component;
import com.example.CableERP.Components.ComponentService;
import com.example.CableERP.enums.Unit;
import com.example.CableERP.exception.DuplicateException;
import com.example.CableERP.exception.WrongValueException;
import com.example.CableERP.Components.ComponentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ComponentServiceUnitTest {

    @Mock
    private ComponentRepository componentRepository;

    @InjectMocks
    private ComponentService componentService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldThrowWrongValueExceptionWhenValueIsNegative() {
        Component component = new Component("Test",null,-10.0);

        assertThrows(WrongValueException.class,
                () -> componentService.addComponent(component));
    }

    @Test
    void shouldThrowDuplicateExceptionWhenComponentNameAlreadyExists() {
        Component component = new Component("Duplicate",Unit.grams,10.0);

        // Simulate repository throwing your Duplicate exception
        when(componentRepository.saveAndFlush(any(Component.class)))
                .thenThrow(new DuplicateException("Component already exists"));

        assertThrows(DuplicateException.class,
                () -> componentService.addComponent(component));
    }

    @Test
    void shouldReturnEmptyListIfNoComponentsInDatabase() {
        when(componentRepository.findAll()).thenReturn(Collections.emptyList());
        List<Component> result = componentService.getComponents();
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void shouldReturnListOfComponentsIfComponentsArePresentInDatabase() {
        Component c1 = new Component("C1",null,5.0);
        Component c2 = new Component("C2",null,15.0);


        when(componentRepository.findAll()).thenReturn(List.of(c1, c2));

        List<Component> result = componentService.getComponents();

        assertEquals(2, result.size());
        assertEquals("C1", result.get(0).getName());
        assertEquals("C2", result.get(1).getName());
    }



}
