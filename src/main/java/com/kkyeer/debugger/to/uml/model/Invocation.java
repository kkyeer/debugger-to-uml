package com.kkyeer.debugger.to.uml.model;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * @Author: kkyeer
 * @Description:
 * @Date:Created in 17:02 2022/12/11
 * @Modified By:
 */
public class Invocation {
    /**
     * name of invoker
     */
    private final String invokerName;
    /**
     * invoke type
     */
    private final InvokeType invokeType;
    /**
     * desc of invocation
     */
    private final String invokeDesc;

    /**
     * name of invoked object
     */
    private final String invokedName;
    /**
     * invocations after this invocation
     */
    private final List<Invocation> followInvocationList;

    private Invocation(Builder builder) {
        invokerName = builder.invokerName;
        invokeType = builder.invokeType;
        invokeDesc = builder.invokeDesc;
        invokedName = builder.invokedName;
        followInvocationList = builder.followInvocationList;
    }

    /**
     * get field
     *
     * @return invokerName
     */
    public String getInvokerName() {
        return this.invokerName;
    }

    /**
     * get field
     *
     * @return invokeType
     */
    public InvokeType getInvokeType() {
        return Optional.ofNullable(this.invokeType).orElse(InvokeType.SIMPLE_INVOKE);
    }

    /**
     * get field
     *
     * @return invokeDesc
     */
    public String getInvokeDesc() {
        return this.invokeDesc;
    }

    /**
     * get field
     *
     * @return invokedName
     */
    public String getInvokedName() {
        return this.invokedName;
    }

    /**
     * get field
     *
     * @return followInvocationList
     */
    public List<Invocation> getFollowInvocationList() {
        return Optional.ofNullable(this.followInvocationList).orElse(Collections.emptyList());
    }


    public static final class Builder {
        private String invokerName;
        private InvokeType invokeType;
        private String invokeDesc;
        private String invokedName;
        private List<Invocation> followInvocationList;

        private Builder() {
        }

        public static Builder builder() {
            return new Builder();
        }

        public Builder invokerName(String invokerName) {
            this.invokerName = invokerName;
            return this;
        }

        public Builder invokeType(InvokeType invokeType) {
            this.invokeType = invokeType;
            return this;
        }

        public Builder invokeDesc(String invokeDesc) {
            this.invokeDesc = invokeDesc;
            return this;
        }

        public Builder invokedName(String invokedName) {
            this.invokedName = invokedName;
            return this;
        }

        public Builder followInvocationList(List<Invocation> followInvocationList) {
            this.followInvocationList = followInvocationList;
            return this;
        }

        public Invocation build() {
            return new Invocation(this);
        }
    }
}
