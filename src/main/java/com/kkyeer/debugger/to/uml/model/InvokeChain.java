package com.kkyeer.debugger.to.uml.model;


import java.util.List;

/**
 * @Author: kkyeer
 * @Description: Invoke Chain
 * @Date:Created in 17:57 2022/12/11
 * @Modified By:
 */
public class InvokeChain {
    private List<Invocation> invokeList;

    public List<Invocation> getInvokeList() {
        return invokeList;
    }

    public void setInvokeList(List<Invocation> invokeList) {
        this.invokeList = invokeList;
    }
}
