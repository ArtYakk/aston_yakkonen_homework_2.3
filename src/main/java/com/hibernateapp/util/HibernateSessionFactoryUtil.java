package com.hibernateapp.util;

import com.hibernateapp.model.User;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class HibernateSessionFactoryUtil {
    private static final Logger log = LoggerFactory.getLogger(HibernateSessionFactoryUtil.class);
    private static SessionFactory sessionFactory;

    public HibernateSessionFactoryUtil() {
    }

    public static SessionFactory getSessionFactory(){
        if(sessionFactory == null){
            try{
                StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                        .configure("hibernate.cfg.xml")
                        .build();

                sessionFactory = new MetadataSources(registry)
                        .addAnnotatedClass(User.class)
                        .buildMetadata()
                        .buildSessionFactory();

            }catch (Exception e){
                System.out.println("Ошибка инициализации SessionFactory: " + e);
                log.error("Ошибка инициализации SessionFactory: {}", e.getMessage());
            }
        }
        return sessionFactory;
    }
}
