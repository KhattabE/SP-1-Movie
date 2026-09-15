package app.config;

import app.utils.Utils;
import jakarta.persistence.EntityManagerFactory;

import java.util.Properties;

public final class HibernateConfig {

    private static volatile EntityManagerFactory emf;

    private HibernateConfig() {}

    public static EntityManagerFactory getEntityManagerFactory() {
        if (emf == null) {
            synchronized (HibernateConfig.class) {
                if (emf == null) {
                    emf = HibernateEmfBuilder.build(buildProps());
                }
            }
        }
        return emf;
    }

    private static Properties buildProps() {
        Properties props = HibernateBaseProperties.createBase();

        // Teaching-friendly default - change to update in production
        props.put("hibernate.hbm2ddl.auto", "create");

        if (System.getenv("DEPLOYED") != null) {
            setDeployedProperties(props);
        } else {
            setDevProperties(props);
        }
        return props;
    }

    private static void setDeployedProperties(Properties props) {
        String dbName = Utils.getPropertyValue("DB_NAME", "config.properties");
        props.setProperty("hibernate.connection.url", System.getenv("JDBC_CONNECTION_STRING").formatted(dbName));
        props.setProperty("hibernate.connection.username", Utils.getPropertyValue("DB_USERNAME", "config.properties"));
        props.setProperty("hibernate.connection.password", System.getenv("JDBC_PASSWORD"));
    }

    private static void setDevProperties(Properties props) {
        String dbName = Utils.getPropertyValue("DB_NAME", "config.properties");
        String username = Utils.getPropertyValue("DB_USERNAME", "config.properties");

        String connectionTemplate = System.getenv("JDBC_CONNECTION_STRING");
        String password = System.getenv("JDBC_PASSWORD");

        if (connectionTemplate == null || password == null) {
            throw new IllegalStateException(
                    "JDBC_CONNECTION_STRING and JDBC_PASSWORD must be configured"
            );
        }

        String connectionString = connectionTemplate.formatted(dbName);

        props.setProperty("hibernate.connection.url", connectionString);
        props.setProperty("hibernate.connection.username", username);
        props.setProperty("hibernate.connection.password", password);
    }
}