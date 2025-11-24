package com.example.CableERP.MRP;

import com.example.CableERP.Component.Component;
import java.util.HashMap;


public record MRPResponseDTO(HashMap<String,HashMap<Component,Double>> stringObjectsHashMap) { }
