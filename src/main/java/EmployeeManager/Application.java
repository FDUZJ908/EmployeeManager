package EmployeeManager;

import org.apache.catalina.Context;
import org.apache.catalina.connector.Connector;
import org.apache.tomcat.util.descriptor.web.SecurityCollection;
import org.apache.tomcat.util.descriptor.web.SecurityConstraint;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.context.annotation.Bean;


@SpringBootApplication
public class Application {
    private static final int httpPort = 80;
    private static final int httpsPort = 443;

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Bean
    public TomcatServletWebServerFactory servletContainer() {
        TomcatServletWebServerFactory tomcat = new TomcatServletWebServerFactory() {
            @Override
            protected void postProcessContext(Context context) {
                SecurityConstraint constraint = new SecurityConstraint();
                constraint.setUserConstraint("CONFIDENTIAL");
                SecurityCollection collection = new SecurityCollection();
                collection.addPattern("/*");
                constraint.addCollection(collection);
                context.addConstraint(constraint);
            }
        };
        tomcat.setPort(httpsPort);
        tomcat.addAdditionalTomcatConnectors(httpConnector());
        return tomcat;
    }

    @Bean
    public Connector httpConnector() {
        Connector connector = new Connector("org.apache.coyote.http11.Http11NioProtocol");
        connector.setScheme("http");
        //Connector监听的http的端口号
        connector.setPort(httpPort);
        connector.setSecure(false);
        //监听到http的端口号后转向到的https的端口号
        connector.setRedirectPort(httpsPort);
        return connector;
    }

/*
    @Bean
    public DataSource dataSource() {
        PoolProperties p = new PoolProperties();
        p.setUrl("jdbc:mysql://localhost:3306/EmployeeManager?useUnicode=true&characterEncoding=UTF-8&useSSL=false");
        p.setUsername("root");
        p.setPassword(System.getenv("DatabasePassword"));
        p.setDriverClassName("com.mysql.jdbc.Driver");
        p.setMaxActive(10);
        p.setInitialSize(5);
        p.setMaxIdle(5);
        p.setMinIdle(1);
        p.setTestWhileIdle(true);
        p.setTestOnBorrow(true);
        p.setValidationQuery("SELECT 1");
        p.setTimeBetweenEvictionRunsMillis(5000);
        p.setMinEvictableIdleTimeMillis(60000);
        return new DataSource(p);
    }
   */
}
