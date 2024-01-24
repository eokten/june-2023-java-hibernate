package hibernate;

import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.transaction.Transactional;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.query.Query;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class HibernateDemo {

    @Transactional
    public static void main(String[] args) {
        try (StandardServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder()
                .configure("hibernate.cfg.xml")
                .build()) {
            Metadata metadata = new MetadataSources(serviceRegistry)
                    .addAnnotatedClass(Product.class)
                    .addAnnotatedClass(ProductDetail.class)
                    .addAnnotatedClass(Category.class)
                    .addAnnotatedClass(Tag.class)
                    .getMetadataBuilder()
                    .build();

            try (SessionFactory sessionFactory = metadata.getSessionFactoryBuilder().build()) {
                try (Session session = sessionFactory.openSession()) {
                    session.getTransaction().begin();

                    Product product1 = new Product();
                    product1.setName("Asus Laptop Model 1");
                    Product product2 = new Product();
                    product2.setName("Asus Laptop Model 2");
                    Product product3 = new Product();
                    product3.setName("Samsung Galaxy Tab 42");

                    ProductDetail detail1 = new ProductDetail();
                    detail1.setDescription("Ноутбук 1 моделі");
                    product1.setProductDetail(detail1);

                    Category category = new Category();
                    category.setName("Електротехніка");
                    category.setProducts(List.of(product1, product2, product3));

                    Tag tag1 = new Tag();
                    tag1.setName("Чорна п'ятниця");
                    Tag tag2 = new Tag();
                    tag2.setName("Новорічні знижки");
                    Tag tag3 = new Tag();
                    tag3.setName("Знижки на моделі минулого року");

                    product1.setTags(List.of(tag2, tag3));
                    product3.setTags(List.of(tag1, tag2));

                    session.persist(tag1);
                    session.persist(tag2);
                    session.persist(tag3);

                    session.persist(category);

                    List<Product> savedProducts = session
                            .createNativeQuery("select * from products", Product.class)
                            .list();

                    System.out.println(savedProducts);

                    session.getTransaction().commit();
                }

                deleteTag(sessionFactory, "Чорна п'ятниця");
            }
        }
        // Liquibase, Flyway
    }

    private static void deleteTag(SessionFactory sessionFactory, String name) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();

            Query<Tag> query = session.createQuery("from Tag tag where tag.name = :name", Tag.class);
            query.setParameter("name", name);
            Tag tag = query.getSingleResult();

            tag.getProducts().forEach(product -> product.getTags().remove(tag));

            session.remove(tag);

            session.getTransaction().commit();
        }
    }
}
