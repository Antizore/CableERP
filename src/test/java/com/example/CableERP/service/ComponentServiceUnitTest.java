package com.example.CableERP.service;


import com.example.CableERP.entity.Component;
import com.example.CableERP.enums.Unit;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class ComponentServiceUnitTest {

    @Mock
    private ComponentService componentService;

    @InjectMocks
    private Component component;


    @Test
    public void shouldThrowWrongValueExceptionWhenValueIsNegative(){

        //  TODO

    }

    @Test
    public void shouldThrowDuplicateErrorWhenComponentNameAlreadyExists(){

        // TODO
    }


}
