/*
 * Copyright (c) 2009, Kent R. Spillner <kspillner@acm.org>
 *
 * Permission to use, copy, modify, and/or distribute this software for any
 * purpose with or without fee is hereby granted, provided that the above
 * copyright notice and this permission notice appear in all copies.
 *
 * THE SOFTWARE IS PROVIDED "AS IS" AND THE AUTHOR DISCLAIMS ALL WARRANTIES
 * WITH REGARD TO THIS SOFTWARE INCLUDING ALL IMPLIED WARRANTIES OF
 * MERCHANTABILITY AND FITNESS. IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR
 * ANY SPECIAL, DIRECT, INDIRECT, OR CONSEQUENTIAL DAMAGES OR ANY DAMAGES
 * WHATSOEVER RESULTING FROM LOSS OF USE, DATA OR PROFITS, WHETHER IN AN
 * ACTION OF CONTRACT, NEGLIGENCE OR OTHER TORTIOUS ACTION, ARISING OUT OF
 * OR IN CONNECTION WITH THE USE OR PERFORMANCE OF THIS SOFTWARE.
 */
package hippie;

import org.junit.runner.Description;
import org.junit.runner.Runner;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunListener;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.InitializationError;

public class NagiosAwareRunner extends Runner {
        private BlockJUnit4ClassRunner delegate;

        public NagiosAwareRunner(Class test) throws InitializationError {
                delegate = new BlockJUnit4ClassRunner(test);
        }

        @Override
        public Description getDescription() {
                return delegate.getDescription();
        }

        @Override
        public void run(final RunNotifier notifier) {
                RunListener listener = new RunListener() {
                        public boolean failed;

                        public String message;

                        @Override
                        public void testStarted(final Description description)
                            throws Exception {
                                failed = false;
                                message = "";
                                super.testStarted(description);
                        }

                        @Override
                        public void testFinished(final Description description)
                            throws Exception {
                                if (failed) {
                                        System.out.println(
                                            "test finished (failed): "
                                                + message);
                                } else {
                                        System.out
                                            .println("test finished (passed)");
                                }
                        }

                        @Override
                        public void testFailure(final Failure failure)
                            throws Exception {
                                failed = true;
                                try {
                                        Description desc =
                                            failure.getDescription();
                                        Nagios ann =
                                            desc.getAnnotation(Nagios.class);
                                        message = ann.value();
                                } catch (Throwable e) {
                                        e.printStackTrace();
                                }
                        }

                        @Override
                        public void testAssumptionFailure(
                            final Failure failure) {
                                System.out.println("test assumption failure");
                        }
                };
                notifier.addListener(listener);
                delegate.run(notifier);
        }
}
