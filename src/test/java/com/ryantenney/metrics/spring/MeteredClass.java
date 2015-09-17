package com.ryantenney.metrics.spring;

import com.codahale.metrics.RatioGauge;
import com.codahale.metrics.annotation.ExceptionMetered;
import com.codahale.metrics.annotation.Metered;
import com.codahale.metrics.annotation.Timed;
import com.ryantenney.metrics.annotation.Counted;

import java.util.concurrent.TimeUnit;

/**
 * Created by karl on 9/17/15.
 */
public class MeteredClass {

  @com.codahale.metrics.annotation.Gauge
  private int gaugedField = 999;

  @com.codahale.metrics.annotation.Gauge
  private RatioGauge gaugedGaugeField = new RatioGauge() {
    @Override
    protected Ratio getRatio() {
      return Ratio.of(1, 2);
    }
  };

  @com.codahale.metrics.annotation.Gauge
  public int gaugedMethod() {
    return this.gaugedField;
  }

  public void setGaugedField(int value) {
    this.gaugedField = value;
  }

  @com.ryantenney.metrics.annotation.CachedGauge(timeout = 1, timeoutUnit = TimeUnit.DAYS)
  public int cachedGaugedMethod() {
    return this.gaugedField;
  }

  @Timed
  public void timedMethod(boolean doThrow) throws Throwable {
    if (doThrow) {
      throw new MeteredClassTest.BogusException();
    }
  }

  @Metered
  public void meteredMethod() {
	}

  @Counted
  public void countedMethod(Runnable runnable) {
    runnable.run();
  }

  @Counted(monotonic = true)
  public void monotonicCountedMethod() {
	}

  @ExceptionMetered(cause = MeteredClassTest.BogusException.class)
  public <T extends Throwable> void exceptionMeteredMethod(Class<T> clazz) throws Throwable {
    if (clazz != null) {
      throw clazz.newInstance();
    }
  }

  @Timed(name = "quadruplyMeteredMethod-timed")
  @Metered(name = "quadruplyMeteredMethod-metered")
  @Counted(name = "quadruplyMeteredMethod-counted")
  @ExceptionMetered(name = "quadruplyMeteredMethod-exceptionMetered", cause = MeteredClassTest.BogusException.class)
  public void quadruplyMeteredMethod(Runnable runnable) throws Throwable {
    runnable.run();
  }

  @Metered(name = "varargs-metered")
  public void varargsMeteredMethod(int... params) {
	}

  @Timed(name = "overloaded-timed")
  public void overloadedTimedMethod() {
	}

  @Timed(name = "overloaded-timed-param")
  public void overloadedTimedMethod(int param) {
	}

  @Metered(name = "overloaded-metered")
  public void overloadedMeteredMethod() {
	}

  @Metered(name = "overloaded-metered-param")
  public void overloadedMeteredMethod(int param) {
	}

  @Counted(name = "overloaded-counted")
  public void overloadedCountedMethod(Runnable runnable) {
    runnable.run();
  }

  @Counted(name = "overloaded-counted-param")
  public void overloadedCountedMethod(int param, Runnable runnable) {
    runnable.run();
  }

  @ExceptionMetered(name = "overloaded-exception-metered", cause = MeteredClassTest.BogusException.class)
  public <T extends Throwable> void overloadedExceptionMeteredMethod(Class<T> clazz) throws Throwable {
    if (clazz != null) {
      throw clazz.newInstance();
    }
  }

  @ExceptionMetered(name = "overloaded-exception-metered-param", cause = MeteredClassTest.BogusException.class)
  public <T extends Throwable> void overloadedExceptionMeteredMethod(Class<T> clazz, int param) throws Throwable {
    if (clazz != null) {
      throw clazz.newInstance();
    }
  }

  @Counted(name = "public-scope-method", monotonic = true)
  public void publicScopeMethod() {
	}

  @Counted(name = "package-scope-method", monotonic = true)
  void packageScopeMethod() {
	}

  @Counted(name = "protected-scope-method", monotonic = true)
  protected void protectedScopeMethod() {
	}

  @Counted(name = "private-scope-method", monotonic = true)
  private void privateScopeMethod() {
    System.out.println("HELL YEAH!");
  }

	public void privateScopedMethodCaller() {
		privateScopeMethod();
	}
}
