package com.kkyeer.debugger.to.uml.model;

/**
 * @Author: kkyeer
 * @Description:
 * @Date:Created in 17:37 2022/12/11
 * @Modified By:
 */
public enum InvokeType {
    SIMPLE_INVOKE("->"),RESPONSE("-->");
    private final String invokeSign;


    InvokeType(String invokeSign) {
        this.invokeSign = invokeSign;
    }

    public String invokeSign() {
        return invokeSign;
    }
}
