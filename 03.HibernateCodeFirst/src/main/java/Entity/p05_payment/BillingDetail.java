package Entity.p05_payment;

import javax.persistence.*;

import Entity.CoreEntity;

@Entity
@Table(name = "billing_details")
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public class BillingDetail extends CoreEntity {
    @Column(nullable = false, unique = true)
    private String number;

    @ManyToOne
    private CorePayment owner;

    public BillingDetail() {
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public CorePayment getOwner() {
        return owner;
    }

    public void setOwner(CorePayment owner) {
        this.owner = owner;
    }
}
