package Entity.p03;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "teachers")
public class Teacher extends CoreUser{
    @Column(unique = true, nullable = false)
    private String email;
    @Column(name = "salary_per_hour")
    private Integer salaryPerHour;

    public Teacher() {
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getSalaryPerHour() {
        return salaryPerHour;
    }

    public void setSalaryPerHour(Integer salaryPerHour) {
        this.salaryPerHour = salaryPerHour;
    }
}
