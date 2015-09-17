package com.ryantenney.metrics.spring;

import com.codahale.metrics.Counter;
import com.codahale.metrics.MetricRegistry;
import com.ryantenney.metrics.annotation.Counted;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

/**
 */
@Aspect
public class MetricsPointcutAspect {
    private MetricRegistry metricRegistry;

    public MetricsPointcutAspect() {
    }

    @Around("execution(* *(..)) && @annotation(counted)")
    public Object countedAnnotated(ProceedingJoinPoint joinPoint, Counted counted) throws Throwable {
        final Counter counter = getMetric(counted, joinPoint);
        counter.inc();
        try {
            return joinPoint.proceed();
        } finally {
            if (!counted.monotonic()) {
                counter.dec();
            }
        }
    }

    private Counter getMetric(Counted name, ProceedingJoinPoint joinPoint) {
        Class<?> klass = joinPoint.getSourceLocation().getWithinType();
        return metricRegistry.counter(Util.forCountedMethod(klass, null, name));
    }

    public void setMetricRegistry(MetricRegistry metricRegistry) {
        this.metricRegistry = metricRegistry;
    }
}
