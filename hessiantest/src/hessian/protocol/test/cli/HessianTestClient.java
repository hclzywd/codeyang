package hessian.protocol.test.cli;

import hessian.protocol.test.api.HessianTestService;
import hessian.protocol.test.api.ParamBean;
import hessian.protocol.test.api.ParamDataBean;

import java.net.MalformedURLException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.alibaba.druid.pool.DruidDataSource;
import com.caucho.hessian.client.HessianProxyFactory;

public class HessianTestClient {
    private static ApplicationContext context ;
    private static final HessianProxyFactory hessianProxyFactory = new HessianProxyFactory();
    private static DruidDataSource wmsDataSource;
    private static final ExecutorService logPool = Executors.newFixedThreadPool(172);
    public static HessianTestService getDubboServiceBean() {
        return (HessianTestService) context.getBean("dubboService") ;
    }
    public static HessianTestService getHessianServiceBean() {
        return (HessianTestService) context.getBean("hessianService") ;
    }
    public static void main(String[] args) {
        wmsDataSource = new DruidDataSource();
        wmsDataSource.setDriverClassName("oracle.jdbc.OracleDriver");
        wmsDataSource.setUsername("wms");
        wmsDataSource.setPassword("wms");
        wmsDataSource.setUrl("jdbc:oracle:thin:@172.16.186.223:1521:sichuan");
        wmsDataSource.setInitialSize(222);
        wmsDataSource.setMinIdle(22);
        wmsDataSource.setMaxActive(600);
        context = new ClassPathXmlApplicationContext("hessian-test-client.xml");
//        for (int i = 0; i < 10; i++) {
//            commonTest() ;
//        }
        commonTest() ;
        doProtocolTest() ;
    }
    
    private static final int[] threadCountArray = {1 ,50 ,100 ,500} ;
    private static final int[] requestBeanCountArray = {1 ,10 ,100} ;
    private static final long TEST_TIME_LONG = (1* 1000 * 60 * 5);
    private static final int threadSleep = 1 ;
    private static final ExecutorService[] POOL_ARRAY = {
        Executors.newFixedThreadPool(1), Executors.newFixedThreadPool(50),
        Executors.newFixedThreadPool(100), Executors.newFixedThreadPool(500)};

    private static void doProtocolTest() {
        //hessian:servlet
        for (int threadCount : threadCountArray) {
            for (int requestBeanCount : requestBeanCountArray) {
                hessianServletTest(threadCount ,requestBeanCount) ;
            }
        }
        //hessian:dubbo
        for (int threadCount : threadCountArray) {
            for (int requestBeanCount : requestBeanCountArray) {
                hessianDubboTest(threadCount ,requestBeanCount) ;
            }
        }
        //dubbo
        for (int threadCount : threadCountArray) {
            for (int requestBeanCount : requestBeanCountArray) {
                dubboTest(threadCount ,requestBeanCount) ;
            }
        }
        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>.... ALL TEST OVER ......");
        for (ExecutorService pool : POOL_ARRAY) {
            pool.shutdown();
        }
    }
    private static void hessianServletTest(final int threadCount, final int requestBeanCount) {
        System.out.println("hessianServletTest,thradCount="+threadCount+",,requestBeanCount="+requestBeanCount+",,,,,, BEGIN ......");
        int pi = 0 ;
        if (threadCount == 50) {
            pi = 1 ;
        }
        if (threadCount == 100) {
            pi = 2 ;
        }
        if (threadCount == 500) {
            pi = 3 ;
        }
        ExecutorService pool =  POOL_ARRAY[pi];
        long start = System.currentTimeMillis() ;
        while (true) {
            try {
                pool.execute(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            String url = "http://172.16.186.141:56789/hessian/test";
                            HessianTestService hessianService =
                              (HessianTestService) hessianProxyFactory.create(HessianTestService.class, url);
                            ParamBean hessianResponseBean = hessianService.hessianRequest(genTestData(requestBeanCount,threadCount,"hessian:servlet")) ;
                            hessianResponseBean.setCliResponseEnd(System.currentTimeMillis());
//                            System.out.println(hessianResponseBean);
                            logToDb(hessianResponseBean);
                        } catch (MalformedURLException e) {
                            e.printStackTrace();
                            System.out.println(">>>>>> Hessian servlet 调用出错 。。 ");
                        }
                    }
                });
                TimeUnit.MILLISECONDS.sleep(threadSleep);
                if ((System.currentTimeMillis() - start) > (TEST_TIME_LONG)) {
                    break ;
                }
            } catch (Exception e) {
                // 
            }
        }
        try {
            TimeUnit.MINUTES.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("hessianServletTest,thradCount="+threadCount+",,requestBeanCount="+requestBeanCount+",,,,,, OVER ......");
    }
    private static void hessianDubboTest(final int threadCount, final int requestBeanCount) {
        System.out.println("hessianDubbo,thradCount="+threadCount+",,requestBeanCount="+requestBeanCount+",,,,,, BEGIN ......");
        int pi = 0 ;
        if (threadCount == 50) {
            pi = 1 ;
        }
        if (threadCount == 100) {
            pi = 2 ;
        }
        if (threadCount == 500) {
            pi = 3 ;
        }
        ExecutorService pool =  POOL_ARRAY[pi];
        long start = System.currentTimeMillis() ;
        while (true) {
            try {
                pool.execute(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            HessianTestService hessianDubboService = getDubboServiceBean() ;
                            ParamBean dubboHessianResponseBean = hessianDubboService.hessianRequest(genTestData(requestBeanCount,threadCount,"hessian：dubbo")) ;
                            dubboHessianResponseBean.setCliResponseEnd(System.currentTimeMillis());
//                            System.out.println(dubboHessianResponseBean);
                            logToDb(dubboHessianResponseBean);
                        } catch (Exception e) {
                            e.printStackTrace();
                            System.out.println(">>>>>> Hessian dubbo 调用出错 。。 ");
                        }
                    }
                });
                TimeUnit.MILLISECONDS.sleep(threadSleep);
                if ((System.currentTimeMillis() - start) > (TEST_TIME_LONG)) {
                    break ;
                }
            } catch (Exception e) {
                // 
            }
        }
        try {
            TimeUnit.MINUTES.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("hessianDubbo,thradCount="+threadCount+",,requestBeanCount="+requestBeanCount+",,,,,, OVER ......");
    }
    private static void dubboTest(final int threadCount, final int requestBeanCount) {
        System.out.println("dubbo ,thradCount="+threadCount+",,requestBeanCount="+requestBeanCount+",,,,,, BEGIN ......");
        int pi = 0 ;
        if (threadCount == 50) {
            pi = 1 ;
        }
        if (threadCount == 100) {
            pi = 2 ;
        }
        if (threadCount == 500) {
            pi = 3 ;
        }
        ExecutorService pool =  POOL_ARRAY[pi];
        long start = System.currentTimeMillis() ;
        while (true) {
            try {
                pool.execute(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            HessianTestService dubboService = getDubboServiceBean() ;
                            ParamBean dubboResponseBean = dubboService.hessianRequest(genTestData(requestBeanCount,threadCount,"dubbo")) ;
                            dubboResponseBean.setCliResponseEnd(System.currentTimeMillis());
//                            System.out.println(dubboResponseBean);
                            logToDb(dubboResponseBean);
                        } catch (Exception e) {
                            e.printStackTrace();
                            System.out.println(">>>>>> Dubbo 调用出错 。。 ");
                        }
                    }
                });
                TimeUnit.MILLISECONDS.sleep(threadSleep);
                if ((System.currentTimeMillis() - start) > (TEST_TIME_LONG)) {
                    break ;
                }
            } catch (Exception e) {
                // 
            }
        }
        try {
            TimeUnit.MINUTES.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("dubbo ,thradCount="+threadCount+",,requestBeanCount="+requestBeanCount+",,,,,, OVER ......");
    }
    @SuppressWarnings("unused")
    private static void commonTest() {
        testDbConnection() ;
      //dubbo
        try {
            HessianTestService dubboService = getDubboServiceBean() ;
            ParamBean dubboResponseBean = dubboService.hessianRequest(genTestData(1,1,"dubbo")) ;
            dubboResponseBean.setCliResponseEnd(System.currentTimeMillis());
            System.out.println(dubboResponseBean);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(">>>>>> Dubbo 调用出错 。。 ");
        }
       
        //hessian：dubbo
        try {
            HessianTestService hessianDubboService = getDubboServiceBean() ;
            ParamBean dubboHessianResponseBean = hessianDubboService.hessianRequest(genTestData(1,1,"hessian：dubbo")) ;
            dubboHessianResponseBean.setCliResponseEnd(System.currentTimeMillis());
            System.out.println(dubboHessianResponseBean);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(">>>>>> Hessian dubbo 调用出错 。。 ");
        }
        //hessian:servlet
        try {
            String url = "http://172.16.186.141:56789/hessian/test";
            HessianTestService hessianService =
              (HessianTestService) hessianProxyFactory.create(HessianTestService.class, url);
            ParamBean hessianResponseBean = hessianService.hessianRequest(genTestData(1,1,"hessian:servlet")) ;
            hessianResponseBean.setCliResponseEnd(System.currentTimeMillis());
            System.out.println(hessianResponseBean);
        } catch (MalformedURLException e) {
            e.printStackTrace();
            System.out.println(">>>>>> Hessian servlet 调用出错 。。 ");
        }
        //序号重置
        no = 1 ;
    }
    private static final ParamBean genTestData(int requestBeanCount ,int concurrentThreadCount ,String protocol){
        if (requestBeanCount < 0) {
            requestBeanCount = 1 ;
        }
        Long curr = System.currentTimeMillis() ;
        Date dt = new Date(curr) ;
        ParamBean paramBean = new ParamBean() ;
        paramBean.setRequestNo(getNo());
        paramBean.setCliRequestStart(curr);
        paramBean.setCliResponseEnd(0L);
        paramBean.setRequestSuccess(false);
        paramBean.setConcurrentThreadCount(concurrentThreadCount);
        paramBean.setProtocol(protocol);
        paramBean.setRequestBeanCount(requestBeanCount);
        List<ParamDataBean> dl = new ArrayList<ParamDataBean>() ;
        String ds = "填充字符串，hessian.protocol.test.cli.HessianTestClient.genTestData(int, int, String)" ;
        for (int i=0 ;i<requestBeanCount ;i++) {
            ParamDataBean dataBean = new ParamDataBean() ;
            dataBean.setParam1(curr);
            dataBean.setParam2(curr);
            dataBean.setParam3(curr);
            dataBean.setParam4(curr);
            dataBean.setParam5(curr);
            dataBean.setParam6(curr);
            dataBean.setParam7(curr);
            dataBean.setParam8(ds);
            dataBean.setParam9(ds);
            dataBean.setParam10(ds);
            dataBean.setParam11(ds);
            dataBean.setParam12(ds);
            dataBean.setParam13(dt);
            dataBean.setParam14(dt);
            dataBean.setParam15(true);
        }
        paramBean.setRequestDataList(dl);
        paramBean.setRequestDataLength(0L); //最后使用一个对象，估算一下，统一赋值更新
        paramBean.setResponseDataLength(0L); //最后使用一个对象，估算一下，统一赋值更新
        paramBean.setServerProcessTime(0L);
        
        return paramBean ;
    }
    private static long no = 1 ;
    private static synchronized final long getNo(){
        return no++ ;
    }
    private static final void logToDb(final ParamBean bean){
        try {
            logPool.execute(new Runnable() {
                @Override
                public void run() {
                    Connection connection = null;
                    PreparedStatement pst = null;
                    try {
                        connection = wmsDataSource.getConnection();
                        StringBuilder sqlBuilder =
                            new StringBuilder(
                                "insert into HESSIAN_TEST_LOG(requestno,protocol,clirequeststart,cliresponseend,serverprocesstime,concurrentthreadcount,requestbeancount,requestdatalength,responsedatalength,requestsuccess) ")
                                .append("values(?,?,?,?,?,?,?,?,?,?) ") ;
                        pst = connection.prepareStatement(sqlBuilder.toString());
                        pst.setLong(1, bean.getRequestNo());
                        pst.setString(2, bean.getProtocol());
                        pst.setLong(3, bean.getCliRequestStart());
                        pst.setLong(4, bean.getCliResponseEnd());
                        pst.setLong(5, bean.getServerProcessTime());
                        pst.setLong(6, bean.getConcurrentThreadCount());
                        pst.setLong(7, bean.getRequestBeanCount());
                        pst.setLong(8, bean.getRequestDataLength());
                        pst.setLong(9, bean.getResponseDataLength());
                        pst.setString(10, bean.getRequestSuccess().toString());
                        pst.executeUpdate();
                    } catch (Exception e) {
                        e.printStackTrace();// 直接输出控制台，不能使用handleException方法，回出现闭环
                    } finally {
                        if (pst != null) {
                            try {
                                pst.close();
                            } catch (SQLException e) {
                                e.printStackTrace();// 直接输出控制台，不能使用handleException方法，回出现闭环
                            }
                        }
                        if (connection != null) {
                            try {
                                connection.close();
                            } catch (SQLException e) {
                                e.printStackTrace();// 直接输出控制台，不能使用handleException方法，回出现闭环
                            }
                        }
                    }
                }
            });
        } catch (Exception e) {
            System.out.println(">>>>>> 测试数据入库时，出错 。。 ");
        }
    }
    private static void testDbConnection() {
        Connection connection = null;
        PreparedStatement pst = null;
        ResultSet rs = null ;
        try {
            connection = wmsDataSource.getConnection();
            String sql = "select 1 from dual " ;
            pst = connection.prepareStatement(sql);
            rs = pst.executeQuery() ;
            while (rs.next()) {
                System.out.println("数据库连接正常 ： " + rs.getLong(1));
            }
        } catch (Exception e) {
            e.printStackTrace();// 直接输出控制台，不能使用handleException方法，回出现闭环
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (pst != null) {
                try {
                    pst.close();
                } catch (SQLException e) {
                    e.printStackTrace();// 直接输出控制台，不能使用handleException方法，回出现闭环
                }
            }
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();// 直接输出控制台，不能使用handleException方法，回出现闭环
                }
            }
        }
    }
}
