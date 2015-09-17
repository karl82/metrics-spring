package com.ryantenney.metrics.spring;

import com.codahale.metrics.Timer;
import com.codahale.metrics.Timer.Context;
import com.codahale.metrics.annotation.Timed;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

/**
 */
@Aspect
public class TimedMetricsAspect extends AbstractMetricsAspect<Timed, Timer> {
    public TimedMetricsAspect() {
    }

    @Around("execution(* *(..)) && @annotation(annotation)")
    public Object measureAnnotated(ProceedingJoinPoint joinPoint, Timed annotation) throws Throwable {
        final Timer timer = getMetric(annotation, joinPoint);
        final Context context = timer.time();
        try {
            return joinPoint.proceed();
        } finally {
            context.close();
        }
    }

    @Override
    protected Timer getMetric(Timed annotation, ProceedingJoinPoint joinPoint) {
        final Class<?> klass = joinPoint.getSourceLocation().getWithinType();
        return getMetricRegistry().timer(Util.forTimedMethod(klass, getMethod(joinPoint), annotation));
    }

}
