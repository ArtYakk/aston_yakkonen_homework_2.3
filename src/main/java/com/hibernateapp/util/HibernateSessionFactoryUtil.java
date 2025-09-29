package com.hibernateapp.util;

import com.hibernateapp.model.User;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.Map;
import java.util.Properties;


public class HibernateSessionFactoryUtil {
    private final SessionFactory sessionFactory;

    public HibernateSessionFactoryUtil() {
        this(new Properties());
    }

    public HibernateSessionFactoryUtil(Properties customProperties) {
        StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                .configure("hibernate.cfg.xml")
                .applySettings(customProperties)
                .build();

        this.sessionFactory = new MetadataSources(registry)
                .addAnnotatedClass(User.class)
                .buildMetadata()
                .buildSessionFactory();
    }

    public SessionFactory getSessionFactory() {
        return sessionFactory;
    }
}