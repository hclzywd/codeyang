package hessian.protocol.test.server;

import hessian.protocol.test.api.HessianTestService;
import hessian.protocol.test.api.ParamBean;

public class HessianTestServiceImpl implements HessianTestService {

    @Override
    public ParamBean hessianRequest(ParamBean param) {
        long curr = System.currentTimeMillis() ;
        param.setServerProcessTime(curr);
        param.setRequestSuccess(true);
        System.out.println("Call From " + param.getProtocol() + " ,, " + param.getServerProcessTime());
        return param;
    }

}
