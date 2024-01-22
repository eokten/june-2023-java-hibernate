package hibernate;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

import java.util.List;

public class HibernateDemo {

    public static void main(String[] args) {
        try (StandardServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder()
                .configure("hibernate.cfg.xml")
                .build()) {
            Metadata metadata = new MetadataSources(serviceRegistry)
                    .addAnnotatedClass(Product.class)
                    .getMetadataBuilder()
                    .build();

            try (SessionFactory sessionFactory = metadata.getSessionFactoryBuilder().build();
                 Session session = sessionFactory.openSession()) {

                Product laptop = new Product();
                laptop.setName("ноутбук");

                session.persist(laptop);
                System.out.println("Product created");

                Product product = session.find(Product.class, 1);
                System.out.println("Product from database: " + product);

                List<Product> savedProducts = session.createNativeQuery("select * from products", Product.class).list();
                System.out.println(savedProducts);
            }
        }

        // Liquibase, Flyway
    }
}
