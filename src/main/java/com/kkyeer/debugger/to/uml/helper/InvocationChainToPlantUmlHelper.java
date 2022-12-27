package com.kkyeer.debugger.to.uml.helper;

import com.kkyeer.debugger.to.uml.model.Invocation;
import com.kkyeer.debugger.to.uml.model.InvokeChain;
import com.kkyeer.debugger.to.uml.model.InvokeType;

import java.util.Objects;

/**
 * @Author: kkyeer
 * @Description:
 * @Date:Created in 18:06 2022/12/11
 * @Modified By:
 */
public class InvocationChainToPlantUmlHelper {
    private static final String SPACE =" ";

    private static final String HEADER = "@startuml\n" +
            "skinparam responseMessageBelowArrow true\n";

    public static String generatePlantUmlText(InvokeChain invokeChain) {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(HEADER);
        for (Invocation invocation : invokeChain.getInvokeList()) {
            fillTextFromInvocation(invocation, stringBuffer);
        }
        stringBuffer.append("@enduml\n");
        return stringBuffer.toString();
    }

    public static void fillTextFromInvocation(Invocation invocation, StringBuffer stringBuffer) {
        // Alice -> Bob: Authentication Request\n
        stringBuffer.append(invocation.getInvokerName())
                .append(SPACE)
                .append(invocation.getInvokeType().invokeSign())
                .append(SPACE)
                .append(invocation.getInvokedName())
                .append(":").append(SPACE)
                .append(invocation.getInvokeDesc())
                .append("\n");
        // activate A
        stringBuffer.append("activate ").append(invocation.getInvokedName()).append("\n");
        for (Invocation followInvocation : invocation.getFollowInvocationList()) {
            fillTextFromInvocation(followInvocation,stringBuffer);
        }
        // Bob --> Alice: Authentication Response\n
        if (!Objects.equals(invocation.getInvokerName(), invocation.getInvokedName())) {
            stringBuffer.append(invocation.getInvokedName())
                    .append(SPACE)
                    .append(InvokeType.RESPONSE.invokeSign())
                    .append(SPACE)
                    .append(invocation.getInvokerName())
                    .append(":").append(SPACE)
                    .append(invocation.getInvokeDesc())
                    .append("\n");
        }

        // deactivate A
        stringBuffer.append("deactivate ").append(invocation.getInvokedName()).append("\n");
    }
}
