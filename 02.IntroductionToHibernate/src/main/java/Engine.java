import javax.persistence.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Engine implements Runnable {

    private final EntityManager entityManager;
    private final BufferedReader reader;

    public Engine(EntityManager entityManager) {
        this.entityManager = entityManager;
        this.reader = new BufferedReader(new InputStreamReader(System.in));
    }

    @Override
    public void run() {

        System.out.println("Select exercise number (starting from 2) :");

        try {
            int exerciseNumber = Integer.parseInt(reader.readLine());

            switch (exerciseNumber) {
                case 2 -> exerciseTwo();
                case 3 -> exerciseThree();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            entityManager.close();
        }
    }

    private void exerciseThree() {
    }

    private void exerciseTwo() {
        entityManager.getTransaction().begin();
        Query query = entityManager.createQuery
                ("UPDATE Town t SET t.name = upper(t.name)" +
                        "WHERE LENGTH(t.name) <= 5 ");

        System.out.println(query.executeUpdate() + " rows affected");
        entityManager.getTransaction().commit();
    }
}
