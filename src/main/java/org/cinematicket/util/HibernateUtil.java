package org.cinematicket.util;

import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

public class HibernateUtil {

    private static final SessionFactory sessionFactory = buildSessionFactory();

    private static SessionFactory buildSessionFactory() {
        // Cria o registry a partir do arquivo hibernate.cfg.xml
        final StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                .configure() // por padrão procura hibernate.cfg.xml no resources
                .build();
        try {
            // Cria SessionFactory a partir do metadata
            return new MetadataSources(registry).buildMetadata().buildSessionFactory();
        } catch (Exception e) {
            // Se der erro, destrói registry para evitar vazamento
            StandardServiceRegistryBuilder.destroy(registry);
            e.printStackTrace();
            throw new ExceptionInInitializerError("Falha ao criar SessionFactory: " + e);
        }
    }

    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    public static void shutdown() {
        getSessionFactory().close();
    }
}
