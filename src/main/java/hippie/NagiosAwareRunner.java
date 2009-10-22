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

import hippie.configurations.Configuration;
import hippie.listeners.NagiosAwareListener;
import hippie.listeners.strategy.OnSuccess;
import hippie.notifiers.NagiosNotifier;
import org.junit.runner.Description;
import org.junit.runner.Runner;
import org.junit.runner.notification.RunListener;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.InitializationError;

/**
 * Understands how to execute JUnit 4.x-style service-monitoring tests.
 */
public class NagiosAwareRunner extends Runner {
        private final Configuration configuration;

        private final Runner delegate;

        public NagiosAwareRunner(final Class test)
            throws InitializationError {
                this(new BlockJUnit4ClassRunner(test),
                    new Configuration(System.getProperties()));
        }

        private NagiosAwareRunner(final Runner delegate,
            final Configuration configuration) {
                this.delegate = delegate;
                this.configuration = configuration;
        }

        @Override
        public Description getDescription() {
                return delegate.getDescription();
        }

        @Override
        public void run(final RunNotifier notifier) {
                final RunListener listener = initializeListener();
                notifier.addListener(listener);
                delegate.run(notifier);
        }

        private RunListener initializeListener() {
                final String nagiosServer =
                    configuration.getNagiosServer();
                final String nagiosPassword =
                    configuration.getNagiosPassword();
                final int nagiosPort = configuration.getNagiosPort();
                final int connectionTimeout =
                    configuration.getConnectionTimeout();
                final int responseTimeout =
                    configuration.getResponseTimeout();

                final NagiosNotifier notifier =
                    new NagiosNotifier(nagiosServer, nagiosPassword,
                        nagiosPort, connectionTimeout, responseTimeout);
                final OnSuccess successStrategy =
                    new OnSuccess(notifier);

                return new NagiosAwareListener(notifier,
                    successStrategy);
        }
}
