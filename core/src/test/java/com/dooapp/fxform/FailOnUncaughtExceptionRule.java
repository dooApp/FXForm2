package com.dooapp.fxform;

import org.junit.Assert;
import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

/**
 * User: Antoine Mischler <antoine@dooapp.com>
 * Date: 22/09/2017
 * Time: 14:43
 */
public class FailOnUncaughtExceptionRule implements TestRule {

    @Override
    public Statement apply(Statement statement, Description description) {
        return new Statement() {
            @Override
            public void evaluate() throws Throwable {
                final boolean[] failed = {false};
                final Throwable[] exception = {null};
                final Thread[] threads = {null};
                Thread.UncaughtExceptionHandler original = Thread.getDefaultUncaughtExceptionHandler();
                Thread.setDefaultUncaughtExceptionHandler((t, e) -> {
                    e.printStackTrace();
                    failed[0] = true;
                    exception[0] = e;
                    threads[0] = t;
                });
                statement.evaluate();
                if (failed[0]) {
                    Assert.fail("Uncaught exception in " + threads[0].getName());
                }
                // restore original uncaught exception handler
                Thread.setDefaultUncaughtExceptionHandler(original);
            }
        };
    }

}
