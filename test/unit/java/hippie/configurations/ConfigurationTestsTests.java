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
package hippie.configurations;

import junit.framework.JUnit4TestAdapter;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;

import java.util.Properties;

public class ConfigurationTestsTests {
        @Mock
        private Properties properties;

        @Before
        public void initializeMocks() throws Exception {
                MockitoAnnotations.initMocks(this);

                when(properties.getProperty("hippie.nsca.port", "5667"))
                    .thenReturn("5667");
                when(properties.getProperty(
                    "hippie.nsca.timeout.connection", "5000"))
                    .thenReturn("5000");
                when(properties.getProperty(
                    "hippie.nsca.timeout.response", "15000"))
                    .thenReturn("15000");
        }

        @Test
        public void shouldGetNSCAServerValueFromSystemProperties()
            throws Exception {
                when(properties.getProperty("hippie.nsca.server"))
                    .thenReturn("localhost");

                final Configuration configuration =
                    new Configuration(properties);

                configuration.getNagiosServer();

                verify(properties).getProperty("hippie.nsca.server");
        }

        @Test
        public void shouldThrowIllegalStateExceptionWhenNSCAServerPropertyIsNotSet()
            throws Exception {
                when(properties.getProperty("hippie.nsca.server"))
                    .thenReturn(null);

                final Configuration configuration =
                    new Configuration(properties);

                try {
                        configuration.getNagiosServer();
                        fail(
                            "should throw IllegalStateException when hippie.nsca.server property is not set");
                } catch (Exception e) {
                        assertEquals(
                            "The property hippie.nsca.server is not set",
                            e.getMessage());
                }
        }

        @Test
        public void shouldThrowIllegalStateExceptionWhenNSCAServerPropertyIsEmpty()
            throws Exception {
                when(properties.getProperty("hippie.nsca.server"))
                    .thenReturn("");

                final Configuration configuration =
                    new Configuration(properties);

                try {
                        configuration.getNagiosServer();
                        fail(
                            "should throw IllegalStateException when hippie.nsca.server property is empty");
                } catch (Exception e) {
                        assertEquals(
                            "The property hippie.nsca.server is empty",
                            e.getMessage());
                }
        }

        @Test
        public void shouldThrowIllegalStateExceptionWhenNSCAServerPropertyIsBlank()
            throws Exception {
                when(properties.getProperty("hippie.nsca.server"))
                    .thenReturn(" \t\r\n");

                final Configuration configuration =
                    new Configuration(properties);

                try {
                        configuration.getNagiosServer();
                        fail(
                            "should throw IllegalStateException when hippie.nsca.server property is blank");
                } catch (Exception e) {
                        assertEquals(
                            "The property hippie.nsca.server is empty",
                            e.getMessage());
                }
        }

        @Test
        public void shouldGetNSCAPasswordValueFromSystemProperties()
            throws Exception {
                when(properties.getProperty("hippie.nsca.password"))
                    .thenReturn("password");

                final Configuration configuration =
                    new Configuration(properties);

                configuration.getNagiosPassword();

                verify(properties).getProperty("hippie.nsca.password");
        }

        @Test
        public void shouldThrowIllegalStateExceptionWhenNSCAPasswordPropertyIsNotSet()
            throws Exception {
                when(properties.getProperty("hippie.nsca.password"))
                    .thenReturn(null);

                final Configuration configuration =
                    new Configuration(properties);

                try {
                        configuration.getNagiosPassword();
                        fail(
                            "should throw IllegalStateException when hippie.nsca.password property is not set");
                } catch (Exception e) {
                        assertEquals(
                            "The property hippie.nsca.password is not set",
                            e.getMessage());
                }
        }

        @Test
        public void shouldThrowIllegalStateExceptionWhenNSCAPasswordPropertyIsEmpty()
            throws Exception {
                when(properties.getProperty("hippie.nsca.password"))
                    .thenReturn("");

                final Configuration configuration =
                    new Configuration(properties);

                try {
                        configuration.getNagiosPassword();
                        fail(
                            "should throw IllegalStateException when hippie.nsca.password property is empty");
                } catch (Exception e) {
                        assertEquals(
                            "The property hippie.nsca.password is empty",
                            e.getMessage());
                }
        }

        @Test
        public void shouldThrowIllegalStateExceptionWhenNSCAPasswordPropertyIsBlank()
            throws Exception {
                when(properties.getProperty("hippie.nsca.password"))
                    .thenReturn(" \t\r\n");

                final Configuration configuration =
                    new Configuration(properties);

                try {
                        configuration.getNagiosPassword();
                        fail(
                            "should throw IllegalStateException when hippie.nsca.password property is blank");
                } catch (Exception e) {
                        assertEquals(
                            "The property hippie.nsca.password is empty",
                            e.getMessage());
                }
        }

        @Test
        public void shouldGetNSCAPortValueFromSystemProperties()
            throws Exception {
                when(properties.getProperty("hippie.nsca.port", "5667"))
                    .thenReturn("1234");

                final Configuration configuration =
                    new Configuration(properties);

                final int port = configuration.getNagiosPort();

                assertEquals(1234, port);
        }

        @Test
        public void shouldDefaultNSCAPortValueWhenGettingFromSystemProperties()
            throws Exception {
                final Configuration configuration =
                    new Configuration(properties);

                final int port = configuration.getNagiosPort();

                verify(properties)
                    .getProperty("hippie.nsca.port", "5667");

                assertEquals(5667, port);
        }

        @Test
        public void shouldGetConnectionTimeoutValueFromSystemProperties()
            throws Exception {
                when(properties.getProperty(
                    "hippie.nsca.timeout.connection", "5000"))
                    .thenReturn("1234");

                final Configuration configuration =
                    new Configuration(properties);

                final int connectionTimeout =
                    configuration.getConnectionTimeout();

                assertEquals(1234, connectionTimeout);
        }

        @Test
        public void shouldDefaultConnectionTimeoutValueWhenGettingFromSystemProperties()
            throws Exception {
                final Configuration configuration =
                    new Configuration(properties);

                final int connectionTimeout =
                    configuration.getConnectionTimeout();

                verify(properties)
                    .getProperty("hippie.nsca.timeout.connection",
                        "5000");

                assertEquals(5000, connectionTimeout);
        }

        @Test
        public void shouldGetResponseTimeoutValueFromSystemProperties()
            throws Exception {
                when(properties.getProperty(
                    "hippie.nsca.timeout.response", "15000"))
                    .thenReturn("1234");

                final Configuration configuration =
                    new Configuration(properties);

                final int responseTimeout =
                    configuration.getResponseTimeout();

                assertEquals(1234, responseTimeout);
        }

        @Test
        public void shouldDefaultResponseTimeoutValueWhenGettingFromSystemProperties()
            throws Exception {
                final Configuration configuration =
                    new Configuration(properties);

                final int responseTimeout =
                    configuration.getResponseTimeout();

                verify(properties)
                    .getProperty("hippie.nsca.timeout.response",
                        "15000");

                assertEquals(15000, responseTimeout);
        }

        public static junit.framework.Test suite() {
                return new JUnit4TestAdapter(
                    ConfigurationTestsTests.class);
        }
}
