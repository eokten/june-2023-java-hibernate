package hibernate;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.query.Query;

import java.util.List;

public class HibernateDemo {

    public static void main(String[] args) {
        try (StandardServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder()
                .configure("hibernate.cfg.xml")
                .build()) {
            Metadata metadata = new MetadataSources(serviceRegistry)
                    .addAnnotatedClass(Car.class)
                    .getMetadataBuilder()
                    .build();

            try (SessionFactory sessionFactory = metadata.getSessionFactoryBuilder().build();
                 Session session = sessionFactory.openSession()) {

                session.getTransaction().begin();
                session.persist(new Car("asdas", 123, 32424, 2000, Type.SEDAN));
                session.persist(new Car("VladymyrRay", 315, 8245, 2040, Type.SUV));
                session.getTransaction().commit();

//                System.out.println(session.createQuery("select c from Car c", Car.class).getResultList());
//                System.out.println(session.createQuery("select c.model from Car c", String.class).getResultList());
                Query<Car> query =
                        session.createQuery("select c from Car c where c.model=:xModel", Car.class);
                query.setParameter("xModel", "asdas");

                System.out.println(query.getResultList());


            }
        }


    }
}

