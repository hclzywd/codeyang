package hessian.protocol.test.server;

import hessian.protocol.test.api.HessianTestService;

import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.nio.SelectChannelConnector;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.util.thread.QueuedThreadPool;

import com.caucho.hessian.server.HessianServlet;

final class JettyServer {
    private JettyServer() {
    }

    private boolean serverStarted = false;

    private static final JettyServer instance = new JettyServer();

    static synchronized final void startServer()
        throws Exception {
        if (instance.serverStarted) {
            return;
        }
        instance.initServerAndStart();
    }

    private void initServerAndStart()
        throws Exception {
        long start = System.currentTimeMillis() ;
        
        Server server = new Server();
        
        SelectChannelConnector connector1 = new SelectChannelConnector();
        int jettyPort = 56789 ;
        connector1.setPort(jettyPort);
        QueuedThreadPool pool = new QueuedThreadPool(512) ;
        pool.setName("jetty_job_pool");
        connector1.setThreadPool(pool);
        connector1.setName("hessian");
        server.setConnectors(new Connector[]{ connector1 });
        
        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.NO_SESSIONS);
        context.setContextPath("/hessian");
        HessianServlet hessian = new HessianServlet() ;
        hessian.setAPIClass(HessianTestService.class);
        hessian.setService(new HessianTestServiceImpl());
        context.addServlet(new ServletHolder(hessian),"/test");
        server.setHandler(context);
        server.start();
        serverStarted = true;
        System.out.println("--HESSIAN TEST HTTP SERVICE START ON PORT["+jettyPort+"] OVER IN "+(System.currentTimeMillis() - start)+" MS ......OK.");
    }
}

