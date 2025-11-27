package com.example.CableERP.MRP;

import com.example.CableERP.Component.Component;
import com.example.CableERP.Component.ComponentMRPDTO;

import java.util.HashMap;
import java.util.List;


public record MRPResponseDTO(List<ComponentMRPDTO> componentMRPDTOS) { }
