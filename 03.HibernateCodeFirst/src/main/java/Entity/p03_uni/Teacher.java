package Entity.p03_uni;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.Set;

@Entity
@Table(name = "teachers")
public class Teacher extends CoreUser {

    @Column(unique = true, nullable = false)
    private String email;

    @Column(name = "salary_per_hour")
    private Integer salaryPerHour;

    @OneToMany(mappedBy = "teacher")
    private Set<Course> courses;

    public Teacher() {
    }

    public Set<Course> getCourses() {
        return courses;
    }

    public void setCourses(Set<Course> courses) {
        this.courses = courses;
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
