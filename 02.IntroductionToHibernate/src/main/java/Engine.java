import entities.Address;
import entities.Employee;
import entities.Project;

import javax.persistence.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;

public class Engine implements Runnable {

    private final EntityManager entityManager;
    private final BufferedReader reader;

    public Engine(EntityManager entityManager) {
        this.entityManager = entityManager;
        this.reader = new BufferedReader(new InputStreamReader(System.in));
    }

    @Override
    public void run() {

        System.out.println("Select exercise number (starting from 2), " +
                "to end the program type \"end\" :");

        try {
            String exerciseNumber = reader.readLine();
            switch (exerciseNumber) {
                case "2" -> problemTwo();
                case "3" -> problemThree();
                case "4" -> problemFour();
                case "5" -> problemFive();
                case "6" -> problemSix();
                case "7" -> problemSeven();
                case "8" -> problemEight();
                case "9" -> problemNine();
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            entityManager.close();
        }
    }

    private void problemNine() {
        List<Project> resultList = entityManager.createQuery
                ("SELECT p FROM Project p " +
                        "ORDER BY p.name", Project.class)
                .setMaxResults(10)
                .getResultList();

        for (Project project : resultList) {
            System.out.printf("Project name: %s%n " +
                            " \tProject Description: %s%n " +
                            " \tProject Start Date:%s%n " +
                            " \tProject End Date: %s%n ",
                    project.getName(), project.getDescription(),
                    project.getStartDate(), project.getEndDate());
        }
    }

    private void problemEight() throws IOException {
        System.out.println("Enter employee id :");
        int id = Integer.parseInt(reader.readLine());

        Employee employee = entityManager.find(Employee.class, id);
        System.out.printf("%s %s - %s%n"
                , employee.getFirstName()
                , employee.getLastName()
                , employee.getJobTitle());
        employee
                .getProjects()
                .stream()
                .sorted(Comparator.comparing(Project::getName))
                .forEach(p -> System.out.printf("\t%s%n", p.getName()));
    }

    private void problemSeven() {
        List<Address> resultList = entityManager.createQuery("SELECT a FROM Address a " +
                "ORDER BY a.employees.size DESC", Address.class)
                .setMaxResults(10)
                .getResultList();

        for (Address address : resultList) {
            System.out.printf("%s, %s - %d employees%n",
                    address.getText(),
                    address.getTown() == null
                            ? "Unknown" : address.getTown().getName(),
                    address.getEmployees().size());
        }
    }

    private void problemSix() throws IOException {
        System.out.println("Enter employee`s last name :");
        String lastName = reader.readLine();

        Address address = insertAddress("Vitoshka 15");
        try {
            Employee employee = entityManager.createQuery("SELECT e FROM Employee e " +
                    "WHERE e.lastName = :lstName", Employee.class)
                    .setParameter("lstName", lastName)
                    .getSingleResult();

            entityManager.getTransaction().begin();
            employee.setAddress(address);
            entityManager.getTransaction().commit();
            System.out.println(lastName + " added to the new address");

        } catch (NoResultException e) {
            System.out.println("Invalid last name");
        }
    }

    //new POJO class
    private Address insertAddress(String addressText) {
        Address address = new Address();
        address.setText(addressText);

        entityManager.getTransaction().begin();
        entityManager.persist(address);
        entityManager.getTransaction().commit();

        System.out.println();
        return address;
    }

    private void problemFive() {
        List<Employee> resultList = entityManager.createQuery
                ("SELECT e FROM Employee e " +
                        "WHERE e.department.name = :d_name " +
                        "ORDER BY e.salary, e.id", Employee.class)
                .setParameter("d_name", "Research and Development")
                .getResultList();

        for (Employee e : resultList) {
            System.out.printf("%s %s %s - %.2f%n",
                    e.getFirstName(),
                    e.getLastName(),
                    e.getDepartment().getName(),
                    e.getSalary());
        }
    }

    private void problemFour() {
        List<Employee> salary = entityManager.createQuery("SELECT e FROM Employee e " +
                "WHERE e.salary > :slry", Employee.class)
                .setParameter("slry", new BigDecimal(50000))
                .getResultList();

        for (Employee s : salary) {
            System.out.println(s.getFirstName());
        }
    }

    private void problemThree() throws IOException {
        System.out.println("Enter the name :");
        String[] fullName = reader.readLine().split("\\s+");

        String firstName = fullName[0];
        String lastName = fullName[1];

        Long singleResult = entityManager.createQuery("SELECT COUNT (e) FROM Employee e " +
                "WHERE e.firstName = :f_name AND e.lastName = :l_name", Long.class)
                .setParameter("f_name", firstName)
                .setParameter("l_name", lastName)
                .getSingleResult();

        System.out.println(singleResult == 1
                ? "Yes"
                : "No");
    }

    private void problemTwo() {
        entityManager.getTransaction().begin();
        Query query = entityManager.createQuery
                ("UPDATE Town t SET t.name = upper(t.name)" +
                        "WHERE LENGTH(t.name) <= 5 ");

        System.out.println(query.executeUpdate() + " rows affected");
        entityManager.getTransaction().commit();
    }
}
