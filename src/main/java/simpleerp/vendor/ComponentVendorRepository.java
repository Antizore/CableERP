package simpleerp.vendor;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ComponentVendorRepository extends JpaRepository<ComponentVendor, Long> {
}
