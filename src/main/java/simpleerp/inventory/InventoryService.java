package simpleerp.inventory;

import simpleerp.common.exception.WrongValueException;
import simpleerp.component.Component;
import simpleerp.component.ComponentRepository;
import simpleerp.component.ComponentResponseDTO;
import simpleerp.reservation.ReservationService;
import simpleerp.vendor.ShowComponentVendorDTO;
import jakarta.transaction.Transactional;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class InventoryService {

    private final InventoryRepository inventoryRepository;
    private final ComponentRepository componentRepository;
    private final ReservationService reservationService;

    public InventoryService(InventoryRepository inventoryRepository, ComponentRepository componentRepository,
                            @Lazy ReservationService reservationService) {
        this.inventoryRepository = inventoryRepository;
        this.componentRepository = componentRepository;
        this.reservationService = reservationService;
    }



    public List<ShowInventoryDTO> getAllInventory() {
        return inventoryRepository.findAll().stream()
                .map(this::mapToDTO)
                .toList();
    }

    public ShowInventoryDTO getInventoryById(Long id) {
        Inventory inventory = inventoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Inventory item not found id: " + id));
        return mapToDTO(inventory);
    }

    public Inventory getInventoryEntityByComponentId(Long componentId) {
        return inventoryRepository.findByComponentId(componentId)
                .orElseThrow(() -> new RuntimeException("No inventory record for component: " + componentId));
    }


    @Transactional
    public void receiveGoods(Long componentId, double quantity) {
        if (quantity <= 0) throw new WrongValueException("Received quantity must be > 0");

        Inventory inventory = getInventoryEntityByComponentId(componentId);
        inventory.setQtyAvailable(inventory.getQtyAvailable() + quantity);
        inventoryRepository.saveAndFlush(inventory);
        reservationService.reallocateStockForComponent(componentId);
    }

    @Transactional
    public void issueGoods(Long componentId, double quantity) {
        Inventory inventory = getInventoryEntityByComponentId(componentId);

        if (inventory.getQtyAvailable() < quantity) {
            throw new WrongValueException("Not enough physical items to issue for production!");
        }

        inventory.setQtyAvailable(inventory.getQtyAvailable() - quantity);
        double newReserved = Math.max(0, inventory.getQtyReserved() - quantity);
        inventory.setQtyReserved(newReserved);
        inventoryRepository.saveAndFlush(inventory);
    }

    @Transactional
    public Inventory initializeOrUpdateInventory(CreateInventoryDTO dto) {
        if (dto.componentId() == null) throw new WrongValueException("component ID cannot be null");
        if (dto.qtyAvailable() < 0 || dto.qtyReserved() < 0) throw new WrongValueException("Quantity cannot be negative");

        return inventoryRepository.findByComponentId(dto.componentId())
                .map(existing -> {
                    existing.setQtyAvailable(dto.qtyAvailable());
                    existing.setQtyReserved(dto.qtyReserved());
                    return inventoryRepository.save(existing);
                })
                .orElseGet(() -> {
                    Component component = componentRepository.findById(dto.componentId())
                            .orElseThrow(() -> new RuntimeException("component not found"));
                    Inventory newInventory = new Inventory(component, dto.qtyAvailable(), dto.qtyReserved());
                    return inventoryRepository.saveAndFlush(newInventory);
                });
    }


    @Transactional
    public Inventory manualCorrection(Long id, UpdateInventoryDTO dto) {
        Inventory inventory = inventoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Inventory not found"));

        //Jakarta validation in dto is handling that
        //if (dto.qtyAvailable() < 0) throw new WrongValueException("Available quantity cannot be negative");
        inventory.setQtyAvailable(dto.qtyAvailable());
        //if (dto.qtyReserved() < 0) throw new WrongValueException("Reserved quantity cannot be negative");
        inventory.setQtyReserved(dto.qtyReserved());
        return inventoryRepository.saveAndFlush(inventory);
    }

    @Transactional
    public void deleteInventory(Long id) {
        inventoryRepository.deleteById(id);
    }

    private ShowInventoryDTO mapToDTO(Inventory inventory) {
        return new ShowInventoryDTO(
                inventory.getId(),
                inventory.getQtyAvailable(),
                inventory.getQtyReserved(),
                new ComponentResponseDTO(
                        inventory.getComponent().getId(),
                        inventory.getComponent().getName(),
                        inventory.getComponent().getUnit(),
                        inventory.getComponent().getCostPerUnit(),
                        inventory.getComponent().getComponentVendors().stream().map(
                                componentVendor -> new ShowComponentVendorDTO(
                                        componentVendor.getVendor().getId(),
                                        componentVendor.getVendor().getName(),
                                        componentVendor.getPrice(),
                                        componentVendor.isPreferred()
                                )
                        ).toList()
                )
        );
    }
}