package hessian.protocol.test.server;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;


public class HessianTestServer {

    public static void main(String[] args) {
        @SuppressWarnings("unused")
        ApplicationContext context = new ClassPathXmlApplicationContext("hessian-test-service.xml");
        try {
            JettyServer.startServer();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(">>>>>>>> 启动jetty server 出错 。。");
        }
    }

}
