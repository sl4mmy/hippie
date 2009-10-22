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

public class NagiosAwareRunnerTests {
        @Mock
        private Configuration configuration;

        @Mock
        private Runner delegate;

        @Mock
        private RunNotifier junitNotifier;

        @Before
        public void initializeMocks() throws Exception {
                MockitoAnnotations.initMocks(this);

                when(configuration.getNagiosServer())
                    .thenReturn("localhost");
                when(configuration.getNagiosPassword())
                    .thenReturn("password");
                when(configuration.getNagiosPort()).thenReturn(5667);
                when(configuration.getConnectionTimeout())
                    .thenReturn(5000);
                when(configuration.getResponseTimeout())
                    .thenReturn(15000);
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

                verify(configuration).getNagiosServer();
        }

        @Test
        public void shouldGetNagiosPasswordFromConfigurationWhenRunning()
            throws Exception {
                final NagiosAwareRunner runner =
                    instantiate(delegate, configuration);

                runner.run(junitNotifier);

                verify(configuration).getNagiosPassword();
        }

        @Test
        public void shouldGetNagiosPortFromConfigurationWhenRunning()
            throws Exception {
                final NagiosAwareRunner runner =
                    instantiate(delegate, configuration);

                runner.run(junitNotifier);

                verify(configuration).getNagiosPort();
        }

        @Test
        public void shouldGetNagiosConnectionTimeoutFromConfigurationWhenRunning()
            throws Exception {
                final NagiosAwareRunner runner =
                    instantiate(delegate, configuration);

                runner.run(junitNotifier);

                verify(configuration).getConnectionTimeout();
        }

        @Test
        public void shouldGetNagiosResponseTimeoutFromConfigurationWhenRunning()
            throws Exception {
                final NagiosAwareRunner runner =
                    instantiate(delegate, configuration);

                runner.run(junitNotifier);

                verify(configuration).getResponseTimeout();
        }

        public static junit.framework.Test suite() {
                return new JUnit4TestAdapter(
                    NagiosAwareRunnerTests.class);
        }

        private NagiosAwareRunner instantiate(final Runner delegate,
            final Configuration configuration) throws Exception {
                final Constructor<NagiosAwareRunner> constructor =
                    NagiosAwareRunner.class
                        .getDeclaredConstructor(Runner.class,
                            Configuration.class);

                constructor.setAccessible(true);

                return constructor.newInstance(delegate, configuration);
        }
}
