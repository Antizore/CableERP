package simpleerp.Vendor;

import jakarta.persistence.*;
import java.sql.Timestamp;
import java.time.Instant;

@Entity
@Table(name = "vendor")
public class Vendor {

    protected Vendor() {}

    public Vendor(String name, String phone, String email) {
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.createdAt = Timestamp.from(Instant.now());
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String phone;
    private String email;

    @Column(name = "created_at")
    private Timestamp createdAt;

    // Getters and Setters
    public Long getId() { return id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public Timestamp getCreatedAt() { return createdAt; }
}
