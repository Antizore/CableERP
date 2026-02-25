package simpleerp.component;

import jakarta.transaction.Transactional;
import simpleerp.bom.BillOfMaterialsDTO;
import simpleerp.bom.BillOfMaterialsService;
import simpleerp.common.exception.CannotDeleteException;
import simpleerp.common.exception.DuplicateException;
import simpleerp.vendor.ShowComponentVendorDTO;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class ComponentService {

    private final ComponentRepository componentRepository;
    private final BillOfMaterialsService billOfMaterialsService;

    public ComponentService(ComponentRepository componentRepository, BillOfMaterialsService billOfMaterialsService) {
        this.componentRepository = componentRepository;
        this.billOfMaterialsService = billOfMaterialsService;
    }

    @Transactional
    public ComponentResponseDTO addComponent(ComponentCreateDTO dto){
        if(componentRepository.findByName(dto.name()).isPresent()) {
            throw new DuplicateException("component with name: " + dto.name() + " already exists");}

        Component component = componentRepository.saveAndFlush(new Component(dto.name(),dto.unit(), dto.costPerUnit()));
        return mapToDTO(component);
    }


    public List<ComponentResponseDTO> getComponents() {
        return componentRepository.findAll().stream().map(this::mapToDTO).toList();
    }

    public ComponentResponseDTO getComponent(Long id) {
        Component component = componentRepository.findById(id).orElseThrow();
        return mapToDTO(component);
    }

    @Transactional
    public void deleteComponent(Long id){
        Component component = componentRepository.findById(id).orElseThrow();
        List<BillOfMaterialsDTO> dtoList = billOfMaterialsService.getBill(id, component);

        if (dtoList == null || dtoList.isEmpty())
        {
            componentRepository.deleteById(component.getId());
        }
        else {
            throw new CannotDeleteException("Cannot delete components that are actively used in BOM. Delete BOM first.");
        }
    }

    @Transactional
    public ComponentResponseDTO patchComponent(Long id, ComponentUpdateDTO dto){
        Component component = componentRepository.findById(id).orElseThrow();

        Optional.ofNullable(dto.name())
                .filter(name -> !name.isBlank())
                .ifPresent(component::setName);

        Optional.ofNullable(dto.unit())
                .ifPresent(component::setUnit);

        Optional.ofNullable(dto.costPerUnit())
                .filter(costPerUnit -> !Double.isNaN(costPerUnit) && !Double.isInfinite(costPerUnit))
                .ifPresent(component::setCostPerUnit);

        componentRepository.saveAndFlush(component);

        return mapToDTO(component);
    }


    private ComponentResponseDTO mapToDTO(Component component){
        return new ComponentResponseDTO(
                component.getId(),
                component.getName(),
                component.getUnit(),
                component.getCostPerUnit(),
                component.getComponentVendors().stream().map(
                        componentVendor -> new ShowComponentVendorDTO(
                                componentVendor.getVendor().getId(),
                                componentVendor.getVendor().getName(),
                                componentVendor.getPrice(),
                                componentVendor.isPreferred()
                        )
                ).toList()
        );
    }


}
