package com.ryantenney.metrics.spring;

import com.codahale.metrics.MetricRegistry;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;

import java.lang.reflect.Method;

/**
 */
public abstract class AbstractMetricsAspect<A, M> {
    private MetricRegistry metricRegistry;

    static Method getMethod(ProceedingJoinPoint joinPoint) {
        Method method = null;
        if (joinPoint.getSignature() instanceof MethodSignature) {
            final MethodSignature signature = (MethodSignature) joinPoint.getSignature();
            method = signature.getMethod();
        }
        return method;
    }

    public MetricRegistry getMetricRegistry() {
        return metricRegistry;
    }

    public void setMetricRegistry(MetricRegistry metricRegistry) {
        this.metricRegistry = metricRegistry;
    }

    protected abstract M getMetric(A annotation, ProceedingJoinPoint joinPoint);
}
