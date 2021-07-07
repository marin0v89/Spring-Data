package Entity.p04_hospital;


import Entity.CoreEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import java.util.Set;

@Entity(name = "medicaments")
public class Medicament extends CoreEntity {
    @Column(nullable = false, length = 40)
    private String name;

    private Set<Patient> patients;

    @ManyToMany(mappedBy = "prescriptions")
    public Set<Patient> getPatients() {
        return this.patients;
    }

    public void setPatients(Set<Patient> patients) {
        this.patients = patients;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }
}