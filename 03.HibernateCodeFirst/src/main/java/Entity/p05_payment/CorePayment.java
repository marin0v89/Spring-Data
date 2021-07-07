package Entity.p05_payment;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import Entity.CoreEntity;

@Entity
@Table(name = "bank_users")
public class CorePayment extends CoreEntity {
    //first name, last name, email, password, billing details
    @Column(name = "first_name", nullable = false, length = 50)
    private String firstName;
    @Column(name = "last_name", nullable = false, length = 50)
    private String lastName;
    @Column(nullable = false, unique = true)
    private String email;
    @Column(unique = true)
    private String password;

}
