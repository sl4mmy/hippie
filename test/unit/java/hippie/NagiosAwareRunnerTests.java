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

import hippie.listeners.NagiosAwareListener;
import junit.framework.JUnit4TestAdapter;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.Runner;
import org.junit.runner.notification.RunNotifier;
import static org.mockito.Matchers.isA;
import org.mockito.Mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;

import java.lang.reflect.Constructor;
import java.util.Properties;

public class NagiosAwareRunnerTests {
        @Mock
        private Properties configuration;

        @Mock
        private Runner delegate;

        @Mock
        private RunNotifier junitNotifier;

        @Before
        public void initializeMocks() throws Exception {
                MockitoAnnotations.initMocks(this);

                when(configuration.getProperty("hippie.nsca.server"))
                    .thenReturn("localhost");
                when(configuration.getProperty("hippie.nsca.password"))
                    .thenReturn("password");
                when(configuration.getProperty("hippie.nsca.port",
                    "5667")).thenReturn("5667");
                when(configuration.getProperty(
                    "hippie.nsca.timeout.connection", "5000"))
                    .thenReturn("5000");
                when(configuration.getProperty(
                    "hippie.nsca.timeout.response", "15000"))
                    .thenReturn("15000");
        }

        @Test
        public void shouldBeAnInstanceOfRunner() throws Exception {
                final NagiosAwareRunner runner =
                    new NagiosAwareRunner(this.getClass());

                assertEquals(true, runner instanceof Runner);
        }

        @Test
        public void shouldDelegateWhenGettingDescription()
            throws Exception {
                final NagiosAwareRunner runner =
                    instantiate(delegate, configuration);

                runner.getDescription();

                verify(delegate).getDescription();
        }

        @Test
        public void shouldDelegateWhenRunning() throws Exception {
                final NagiosAwareRunner runner =
                    instantiate(delegate, configuration);

                runner.run(junitNotifier);

                verify(delegate).run(junitNotifier);
        }

        @Test
        public void shouldAddNagiosAwareListenerToRunNotifierWhenRunning()
            throws Exception {
                final NagiosAwareRunner runner =
                    instantiate(delegate, configuration);

                runner.run(junitNotifier);

                verify(junitNotifier)
                    .addListener(isA(NagiosAwareListener.class));
        }

        @Test
        public void shouldGetNagiosServerFromConfigurationWhenRunning()
            throws Exception {
                final NagiosAwareRunner runner =
                    instantiate(delegate, configuration);

                runner.run(junitNotifier);

                verify(configuration).getProperty("hippie.nsca.server");
        }

        @Test
        public void shouldGetNagiosPasswordFromConfiguration()
            throws Exception {
                final NagiosAwareRunner runner =
                    instantiate(delegate, configuration);

                runner.run(junitNotifier);

                verify(configuration)
                    .getProperty("hippie.nsca.password");
        }

        @Test
        public void shouldGetNagiosPortWithDefaultOf5667FromConfigurationWhenRunning()
            throws Exception {
                final NagiosAwareRunner runner =
                    instantiate(delegate, configuration);

                runner.run(junitNotifier);

                verify(configuration)
                    .getProperty("hippie.nsca.port", "5667");
        }

        @Test
        public void shouldGetNagiosConnectionTimeoutWithDefaultOf5000MillisecondsFromConfigurationWhenRunning()
            throws Exception {
                final NagiosAwareRunner runner =
                    instantiate(delegate, configuration);

                runner.run(junitNotifier);

                verify(configuration)
                    .getProperty("hippie.nsca.timeout.connection",
                        "5000");
        }

        @Test
        public void shouldGetNagiosResponseTimeoutWithDefaultOf15000MillisecondsFromConfigurationWhenRunning()
            throws Exception {
                final NagiosAwareRunner runner =
                    instantiate(delegate, configuration);

                runner.run(junitNotifier);

                verify(configuration)
                    .getProperty("hippie.nsca.timeout.response",
                        "15000");
        }

        public static junit.framework.Test suite() {
                return new JUnit4TestAdapter(
                    NagiosAwareRunnerTests.class);
        }

        private NagiosAwareRunner instantiate(final Runner delegate,
            final Properties configuration) throws Exception {
                final Constructor<NagiosAwareRunner> constructor =
                    NagiosAwareRunner.class
                        .getDeclaredConstructor(Runner.class,
                            Properties.class);

                constructor.setAccessible(true);

                return constructor.newInstance(delegate, configuration);
        }
}
