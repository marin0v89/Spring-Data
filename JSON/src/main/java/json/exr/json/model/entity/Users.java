package json.exr.json.model.entity;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "users")
public class Users extends BaseEntity {
    private String firstName;
    private String lastName;
    private Integer age;
    private Set<Users> friends;
    private Set<Products> soldProducts;

    public Users() {
    }

    @Column(name = "first_name")
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    @Column(name = "last_name", nullable = false)
    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    @Column
    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    @ManyToMany
    public Set<Users> getFriends() {
        return friends;
    }

    public void setFriends(Set<Users> friends) {
        this.friends = friends;
    }

    @OneToMany(mappedBy = "seller", fetch = FetchType.EAGER)
    public Set<Products> getSoldProducts() {
        return soldProducts;
    }

    public void setSoldProducts(Set<Products> soldProducts) {
        this.soldProducts = soldProducts;
    }
}
