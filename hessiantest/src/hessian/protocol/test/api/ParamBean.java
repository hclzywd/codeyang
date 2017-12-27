package hessian.protocol.test.api;

import java.io.Serializable;
import java.util.List;

public class ParamBean implements Serializable {

    private static final long serialVersionUID = -7091494342812634088L;

    private Long requestNo ; //客户端请求序列号
    private String protocol ; //测试协议：hessian:servlet、dubbo、hessian：dubbo
    private Long cliRequestStart ; //客户端开始请求时间
    private Long cliResponseEnd ; //客户端接收到响应的时间
    private Long serverProcessTime ; //服务器处理的时刻
    private Integer concurrentThreadCount ;//测试并发量：1、50、100、500
    private Integer requestBeanCount ; //客户端请求，包含的数据bean个数：1、10、100
    private Long requestDataLength ; //客户端请求，请求的数据包大小
    private Long responseDataLength ; //客户端请求，响应的数据包大小
    private List<ParamDataBean> requestDataList ; //模拟业务数据包
    private Boolean requestSuccess ; //请求是否成功，true、false
    
    @Override
    public String toString() {
        StringBuilder str = new StringBuilder("<><><><><><>") ;
        str.append("requestNo=").append(requestNo) ;
        str.append(",protocol=").append(protocol) ;
        str.append(",COST=").append(cliResponseEnd - cliRequestStart) ;
        str.append(",cliRequestStart=").append(cliRequestStart) ;
        str.append(",cliResponseEnd=").append(cliResponseEnd) ;
        str.append(",serverProcessTime=").append(serverProcessTime) ;
        str.append(",concurrentThreadCount=").append(concurrentThreadCount) ;
        str.append(",requestBeanCount=").append(requestBeanCount) ;
        str.append(",requestDataLength=").append(cliRequestStart) ;
        str.append(",responseDataLength=").append(responseDataLength) ;
        str.append(",requestSuccess=").append(requestSuccess) ;
        return str.toString();
    }
    
    public final Long getCliRequestStart() {
        return cliRequestStart;
    }
    public final void setCliRequestStart(Long cliRequestStart) {
        this.cliRequestStart = cliRequestStart;
    }
    public final Long getCliResponseEnd() {
        return cliResponseEnd;
    }
    public final void setCliResponseEnd(Long cliResponseEnd) {
        this.cliResponseEnd = cliResponseEnd;
    }
    public final Long getServerProcessTime() {
        return serverProcessTime;
    }
    public final void setServerProcessTime(Long serverProcessTime) {
        this.serverProcessTime = serverProcessTime;
    }
    public final String getProtocol() {
        return protocol;
    }
    public final void setProtocol(String protocol) {
        this.protocol = protocol;
    }
    public final Integer getConcurrentThreadCount() {
        return concurrentThreadCount;
    }
    public final void setConcurrentThreadCount(Integer concurrentThreadCount) {
        this.concurrentThreadCount = concurrentThreadCount;
    }
    public final Long getRequestNo() {
        return requestNo;
    }
    public final void setRequestNo(Long requestNo) {
        this.requestNo = requestNo;
    }
    public final Integer getRequestBeanCount() {
        return requestBeanCount;
    }
    public final void setRequestBeanCount(Integer requestBeanCount) {
        this.requestBeanCount = requestBeanCount;
    }
    public final Long getRequestDataLength() {
        return requestDataLength;
    }
    public final void setRequestDataLength(Long requestDataLength) {
        this.requestDataLength = requestDataLength;
    }
    public final Long getResponseDataLength() {
        return responseDataLength;
    }
    public final void setResponseDataLength(Long responseDataLength) {
        this.responseDataLength = responseDataLength;
    }
    public final List<ParamDataBean> getRequestDataList() {
        return requestDataList;
    }
    public final void setRequestDataList(List<ParamDataBean> requestDataList) {
        this.requestDataList = requestDataList;
    }
    public final Boolean getRequestSuccess() {
        return requestSuccess;
    }
    public final void setRequestSuccess(Boolean requestSuccess) {
        this.requestSuccess = requestSuccess;
    }
}
