package com.example.CableERP.Procurement;


import com.example.CableERP.Common.Exception.DuplicateException;
import org.springframework.stereotype.Service;


import java.util.List;

@Service
public class ProcurementService {


    private final ProcurementRepository procurementRepository;

    public ProcurementService(ProcurementRepository procurementRepository){
        this.procurementRepository = procurementRepository;
    }

    //TODO: LEPSZA WERYFIKACJA
    public void addVendor(Procurement procurement){
        if(!procurementRepository.existsByName(procurement.getName())) procurementRepository.saveAndFlush(procurement);
        else throw new DuplicateException("Vendor o takiej nazwie istnieje");
    }


    public List<Procurement> getVendors(){
        return procurementRepository.findAll();
    }


    public Procurement getVendor(Long id){
        return procurementRepository.findById(id).orElseThrow();
    }

    //TODO: https://stackoverflow.com/questions/17095628/loop-over-all-fields-in-a-java-class
    public void updateVendor(Long id, UpdateProcurementDTO vendorUpdated){
        Procurement procurementFromRepo = procurementRepository.findById(id).orElseThrow();

        if(vendorUpdated.name() != null) procurementFromRepo.setName(vendorUpdated.name());
        if(vendorUpdated.phone() != null) procurementFromRepo.setPhone(vendorUpdated.phone());
        if(vendorUpdated.email() != null) procurementFromRepo.setEmail(vendorUpdated.email());
        if(vendorUpdated.leadTimeDays() != null) procurementFromRepo.setLeadTimeDays(vendorUpdated.leadTimeDays());

        procurementRepository.saveAndFlush(procurementFromRepo);
    }

    public void deleteVendor(Long id){
        procurementRepository.deleteById(id);
    }



}
