package com.example.CableERP.Vendor;


import com.example.CableERP.Common.Exception.DuplicateException;
import org.springframework.stereotype.Service;


import java.util.List;

@Service
public class VendorService {


    private final VendorRepository vendorRepository;

    public VendorService(VendorRepository vendorRepository){
        this.vendorRepository = vendorRepository;
    }

    //TODO: LEPSZA WERYFIKACJA
    public void addVendor(Vendor vendor){
        if(!vendorRepository.existsByName(vendor.getName())) vendorRepository.saveAndFlush(vendor);
        else throw new DuplicateException("Vendor o takiej nazwie istnieje");
    }


    public List<Vendor> getVendors(){
        return vendorRepository.findAll();
    }


    public Vendor getVendor(Long id){
        return vendorRepository.findById(id).orElseThrow();
    }

    //TODO: https://stackoverflow.com/questions/17095628/loop-over-all-fields-in-a-java-class
    public void updateVendor(Long id, UpdateVendorDTO vendorUpdated){
        Vendor vendorFromRepo = vendorRepository.findById(id).orElseThrow();

        if(vendorUpdated.name() != null) vendorFromRepo.setName(vendorUpdated.name());
        if(vendorUpdated.phone() != null) vendorFromRepo.setPhone(vendorUpdated.phone());
        if(vendorUpdated.email() != null) vendorFromRepo.setEmail(vendorUpdated.email());
        if(vendorUpdated.leadTimeDays() != null) vendorFromRepo.setLeadTimeDays(vendorUpdated.leadTimeDays());

        vendorRepository.saveAndFlush(vendorFromRepo);
    }

    public void deleteVendor(Long id){
        vendorRepository.deleteById(id);
    }



}
