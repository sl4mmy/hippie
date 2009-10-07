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
package hippie.listeners;

import hippie.MonitorsService;
import hippie.listeners.strategy.OnSuccess;
import hippie.listeners.strategy.Strategy;
import hippie.notifiers.NagiosNotifier;
import org.junit.runner.Description;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunListener;

/**
 * Understands how to respond appropriately to the result of JUnit
 * 4.x-style service-monitoring tests.
 */
public class NagiosAwareListener extends RunListener {
        private final NagiosNotifier notifier;

        private Strategy strategy;

        public NagiosAwareListener(final NagiosNotifier notifier,
            final OnSuccess successStrategy) {
                this.notifier = notifier;
                this.strategy = successStrategy;
        }

        @Override
        public void testStarted(final Description description)
            throws Exception {
                strategy = strategy.success();
                super.testStarted(description);
        }

        @Override
        public void testFinished(final Description description)
            throws Exception {
                final MonitorsService annotation =
                    getAnnotation(description);

                if (annotation != null) {
                        strategy.execute(annotation);
                }

                super.testFinished(description);
        }

        @Override
        public void testFailure(final Failure failure)
            throws Exception {
                strategy = strategy.failed();
                super.testFailure(failure);
        }

        @Override
        public void testAssumptionFailure(final Failure failure) {
                strategy = strategy.failed();
                super.testAssumptionFailure(failure);
        }

        @Override
        public void testIgnored(final Description description)
            throws Exception {
                strategy = strategy.ignored();
                super.testIgnored(description);
        }

        private MonitorsService getAnnotation(
            final Description description) {
                return description.getAnnotation(MonitorsService.class);
        }
}
