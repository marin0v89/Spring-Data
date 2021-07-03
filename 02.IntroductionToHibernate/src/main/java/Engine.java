import entities.Address;
import entities.Employee;

import javax.persistence.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
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
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            entityManager.close();
        }
    }

    private void problemSeven() {
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

        } catch (NoResultException e) {
            System.out.println("Invalid last name");
        }finally {
            System.out.println(lastName + " added to the new address");
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
