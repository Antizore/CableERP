package com.example.CableERP.MRP;


import com.example.CableERP.Component.ComponentMRPDTO;
import com.fasterxml.jackson.annotation.JsonProperty;


import java.util.List;


public record MRPResponseDTO(
        @JsonProperty("Components")
        List<List<ComponentMRPDTO>> componentMRPDTOS
) { }
