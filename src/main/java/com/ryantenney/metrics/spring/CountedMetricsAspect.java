package com.ryantenney.metrics.spring;

import com.codahale.metrics.Counter;
import com.ryantenney.metrics.annotation.Counted;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

/**
 */
@Aspect
public class CountedMetricsAspect extends AbstractMetricsAspect<Counted, Counter> {
    public CountedMetricsAspect() {
    }


    @Around("execution(* *(..)) && @annotation(annotation)")
    public Object measureAnnotated(ProceedingJoinPoint joinPoint, Counted annotation) throws Throwable {
        final Counter counter = getMetric(annotation, joinPoint);
        counter.inc();
        try {
            return joinPoint.proceed();
        } finally {
            if (!annotation.monotonic()) {
                counter.dec();
            }
        }
    }

    @Override
    protected Counter getMetric(Counted annotation, ProceedingJoinPoint joinPoint) {
        final Class<?> klass = joinPoint.getSourceLocation().getWithinType();
        return getMetricRegistry().counter(Util.forCountedMethod(klass, getMethod(joinPoint), annotation));
    }

}
