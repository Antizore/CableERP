package simpleerp.component;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ComponentRepository extends JpaRepository<Component, Long> {
    Optional<Component> findByName (@Param("name") String name);

    @Query("SELECT DISTINCT c FROM Component c LEFT JOIN FETCH c.componentVendors")
    List<Component> findAllWithVendors();

}
