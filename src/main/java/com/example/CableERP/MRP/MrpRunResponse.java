package com.example.CableERP.MRP;

import java.util.List;


public class MrpRunResponse {

    private final List<RequirementsDTO> grossRequirements;
    private final List<RequirementsDTO> componentsToBuy;
    private final List<RequirementsDTO> componentsToProduce;


    public MrpRunResponse(List<RequirementsDTO> grossRequirements,List<RequirementsDTO> componentsToBuy, List<RequirementsDTO> componentsToProduce){
        this.grossRequirements = grossRequirements;
        this.componentsToBuy = componentsToBuy;
        this.componentsToProduce = componentsToProduce;
    }

    public List<RequirementsDTO> getGrossRequirements() {
        return grossRequirements;
    }

    public List<RequirementsDTO> getComponentsToBuy() {
        return componentsToBuy;
    }

    public List<RequirementsDTO> getComponentsToProduce() {
        return componentsToProduce;
    }
}
