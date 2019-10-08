/**
 * Copyright (c) 2016, 2019, Oracle and/or its affiliates. All rights reserved.
 */
package com.oracle.bmc.dts;

import com.oracle.bmc.dts.requests.*;
import com.oracle.bmc.dts.responses.*;

/**
 * Collection of helper methods to produce {@link com.oracle.bmc.waiter.Waiter}s for different
 * resources of TransferApplianceEntitlement.
 * <p>
 * The default configuration used is defined by {@link com.oracle.bmc.waiter.Waiters.Waiters#DEFAULT_POLLING_WAITER}.
 */
@javax.annotation.Generated(value = "OracleSDKGenerator", comments = "API Version: 1.0.011")
@lombok.RequiredArgsConstructor
public class TransferApplianceEntitlementWaiters {
    private final java.util.concurrent.ExecutorService executorService;
    private final TransferApplianceEntitlement client;

    /**
     * Creates a new {@link com.oracle.bmc.waiter.Waiter} using the default configuration.
     *
     * @param request the request to send
     * @param targetStates the desired states to wait for. If multiple states are provided then the waiter will return once the resource reaches any of the provided states
     * @return a new {@code Waiter} instance
     */
    public com.oracle.bmc.waiter.Waiter<
                    GetTransferApplianceEntitlementRequest, GetTransferApplianceEntitlementResponse>
            forTransferApplianceEntitlement(
                    GetTransferApplianceEntitlementRequest request,
                    com.oracle.bmc.dts.model.TransferApplianceEntitlement.LifecycleState...
                            targetStates) {
        org.apache.commons.lang3.Validate.notEmpty(
                targetStates, "At least one targetState must be provided");
        org.apache.commons.lang3.Validate.noNullElements(
                targetStates, "Null targetState values are not permitted");

        return forTransferApplianceEntitlement(
                com.oracle.bmc.waiter.Waiters.DEFAULT_POLLING_WAITER, request, targetStates);
    }

    /**
     * Creates a new {@link com.oracle.bmc.waiter.Waiter} using the provided configuration.
     *
     * @param request the request to send
     * @param targetState the desired state to wait for
     * @param terminationStrategy the {@link com.oracle.bmc.waiter.TerminationStrategy} to use
     * @param delayStrategy the {@link com.oracle.bmc.waiter.DelayStrategy} to use
     * @return a new {@code com.oracle.bmc.waiter.Waiter} instance
     */
    public com.oracle.bmc.waiter.Waiter<
                    GetTransferApplianceEntitlementRequest, GetTransferApplianceEntitlementResponse>
            forTransferApplianceEntitlement(
                    GetTransferApplianceEntitlementRequest request,
                    com.oracle.bmc.dts.model.TransferApplianceEntitlement.LifecycleState
                            targetState,
                    com.oracle.bmc.waiter.TerminationStrategy terminationStrategy,
                    com.oracle.bmc.waiter.DelayStrategy delayStrategy) {
        org.apache.commons.lang3.Validate.notNull(targetState, "The targetState cannot be null");

        return forTransferApplianceEntitlement(
                com.oracle.bmc.waiter.Waiters.newWaiter(terminationStrategy, delayStrategy),
                request,
                targetState);
    }

    /**
     * Creates a new {@link com.oracle.bmc.waiter.Waiter} using the provided configuration.
     *
     * @param request the request to send
     * @param terminationStrategy the {@link com.oracle.bmc.waiter.TerminationStrategy} to use
     * @param delayStrategy the {@link com.oracle.bmc.waiter.DelayStrategy} to use
     * @param targetStates the desired states to wait for. The waiter will return once the resource reaches any of the provided states
     * @return a new {@code com.oracle.bmc.waiter.Waiter} instance
     */
    public com.oracle.bmc.waiter.Waiter<
                    GetTransferApplianceEntitlementRequest, GetTransferApplianceEntitlementResponse>
            forTransferApplianceEntitlement(
                    GetTransferApplianceEntitlementRequest request,
                    com.oracle.bmc.waiter.TerminationStrategy terminationStrategy,
                    com.oracle.bmc.waiter.DelayStrategy delayStrategy,
                    com.oracle.bmc.dts.model.TransferApplianceEntitlement.LifecycleState...
                            targetStates) {
        org.apache.commons.lang3.Validate.notEmpty(
                targetStates, "At least one target state must be provided");
        org.apache.commons.lang3.Validate.noNullElements(
                targetStates, "Null target states are not permitted");

        return forTransferApplianceEntitlement(
                com.oracle.bmc.waiter.Waiters.newWaiter(terminationStrategy, delayStrategy),
                request,
                targetStates);
    }

    // Helper method to create a new Waiter for TransferApplianceEntitlement.
    private com.oracle.bmc.waiter.Waiter<
                    GetTransferApplianceEntitlementRequest, GetTransferApplianceEntitlementResponse>
            forTransferApplianceEntitlement(
                    com.oracle.bmc.waiter.BmcGenericWaiter waiter,
                    final GetTransferApplianceEntitlementRequest request,
                    final com.oracle.bmc.dts.model.TransferApplianceEntitlement.LifecycleState...
                            targetStates) {
        final java.util.Set<com.oracle.bmc.dts.model.TransferApplianceEntitlement.LifecycleState>
                targetStatesSet = new java.util.HashSet<>(java.util.Arrays.asList(targetStates));

        return new com.oracle.bmc.waiter.internal.SimpleWaiterImpl<>(
                executorService,
                waiter.toCallable(
                        com.google.common.base.Suppliers.ofInstance(request),
                        new com.google.common.base.Function<
                                GetTransferApplianceEntitlementRequest,
                                GetTransferApplianceEntitlementResponse>() {
                            @Override
                            public GetTransferApplianceEntitlementResponse apply(
                                    GetTransferApplianceEntitlementRequest request) {
                                return client.getTransferApplianceEntitlement(request);
                            }
                        },
                        new com.google.common.base.Predicate<
                                GetTransferApplianceEntitlementResponse>() {
                            @Override
                            public boolean apply(GetTransferApplianceEntitlementResponse response) {
                                return targetStatesSet.contains(
                                        response.getTransferApplianceEntitlement()
                                                .getLifecycleState());
                            }
                        },
                        targetStatesSet.contains(
                                com.oracle.bmc.dts.model.TransferApplianceEntitlement.LifecycleState
                                        .Deleted)),
                request);
    }
}
