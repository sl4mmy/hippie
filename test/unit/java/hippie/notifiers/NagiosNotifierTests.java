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
package hippie.notifiers;

import com.googlecode.jsendnsca.core.Level;
import com.googlecode.jsendnsca.core.MessagePayload;
import com.googlecode.jsendnsca.core.NagiosPassiveCheckSender;
import com.googlecode.jsendnsca.core.NagiosSettings;
import static junit.framework.Assert.assertEquals;
import junit.framework.JUnit4TestAdapter;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;

import java.io.PrintStream;
import java.lang.reflect.Field;

public class NagiosNotifierTests {
        @Mock
        private NagiosPassiveCheckSender delegate;

        @Before
        public void initializeMocks() throws Exception {
                MockitoAnnotations.initMocks(this);
        }

        @Test
        public void shouldSetDelegate() throws Exception {
                final NagiosSettings settings = new NagiosSettings();
                final NagiosPassiveCheckSender delegate =
                    new NagiosPassiveCheckSender(settings);

                final NagiosNotifier notifier =
                    new NagiosNotifier(delegate);

                assertEquals(delegate, notifier.getDelegate());
        }

        @Test
        public void shouldSetSettingsOnDelegate() throws Exception {
                final NagiosSettings settings = new NagiosSettings();

                final NagiosNotifier notifier =
                    new NagiosNotifier(settings);

                assertEquals(settings,
                    getSettings(notifier.getDelegate()));
        }

        @Test
        public void shouldSetNSCAServerOnDelegate() throws Exception {
                final NagiosNotifier notifier =
                    new NagiosNotifier("NSCA SERVER", "NSCA PASSWORD",
                        123, 456, 789);

                final NagiosSettings settings =
                    getSettings(notifier.getDelegate());

                assertEquals("NSCA SERVER", settings.getNagiosHost());
        }

        @Test
        public void shouldSetNSCAPasswordOnDelegate() throws Exception {
                final NagiosNotifier notifier =
                    new NagiosNotifier("NSCA SERVER", "NSCA PASSWORD",
                        123, 456, 789);

                final NagiosSettings settings =
                    getSettings(notifier.getDelegate());

                assertEquals("NSCA PASSWORD", settings.getPassword());
        }

        @Test
        public void shouldSetNSCAPortOnDelegate() throws Exception {
                final NagiosNotifier notifier =
                    new NagiosNotifier("NSCA SERVER", "NSCA PASSWORD",
                        123, 456, 789);

                final NagiosSettings settings =
                    getSettings(notifier.getDelegate());

                assertEquals(123, settings.getPort());
        }

        @Test
        public void shouldSetNSCAConnectionTimeoutOnDelegate()
            throws Exception {
                final NagiosNotifier notifier =
                    new NagiosNotifier("NSCA SERVER", "NSCA PASSWORD",
                        123, 456, 789);

                final NagiosSettings settings =
                    getSettings(notifier.getDelegate());

                assertEquals(456, settings.getConnectTimeout());
        }

        @Test
        public void shouldSetNSCAResponseTimeoutOnDelegate()
            throws Exception {
                final NagiosNotifier notifier =
                    new NagiosNotifier("NSCA SERVER", "NSCA PASSWORD",
                        123, 456, 789);

                final NagiosSettings settings =
                    getSettings(notifier.getDelegate());

                assertEquals(789, settings.getTimeout());
        }

        @Test
        public void shouldSetServiceNameOnPayload() throws Exception {
                final NagiosNotifier notifier =
                    new NagiosNotifier(delegate);

                final MessagePayload payload = notifier
                    .createMessagePayload("SERVICE NAME",
                        "SERVICE HOST", Level.OK, "MESSAGE");

                assertEquals("SERVICE NAME", payload.getServiceName());
        }

        @Test
        public void shouldSetServiceHostOnPayload() throws Exception {
                final NagiosNotifier notifier =
                    new NagiosNotifier(delegate);

                final MessagePayload payload = notifier
                    .createMessagePayload("SERVICE NAME",
                        "SERVICE HOST", Level.OK, "MESSAGE");

                assertEquals("SERVICE HOST", payload.getHostname());
        }

        @Test
        public void shouldSetLevelOnPayload() throws Exception {
                final NagiosNotifier notifier =
                    new NagiosNotifier(delegate);

                final MessagePayload payload = notifier
                    .createMessagePayload("SERVICE NAME",
                        "SERVICE HOST", Level.OK, "MESSAGE");

                assertEquals(0, payload.getLevel());
        }

        @Test
        public void shouldSetMessageOnPayload() throws Exception {
                final NagiosNotifier notifier =
                    new NagiosNotifier(delegate);

                final MessagePayload payload = notifier
                    .createMessagePayload("SERVICE NAME",
                        "SERVICE HOST", Level.OK, "MESSAGE");

                assertEquals("MESSAGE", payload.getMessage());
        }

        @Test
        public void shouldDelegateWhenSending() throws Exception {
                final NagiosNotifier notifier =
                    new NagiosNotifier(delegate);

                final MessagePayload payload =
                    mock(MessagePayload.class);

                notifier.send(payload);

                verify(delegate).send(payload);
        }

        @Test
        public void shouldPrintExceptionMessageToStandardErrorWhenExceptionsAreThrownWhileSending()
            throws Exception {
                final PrintStream stderr = mock(PrintStream.class);
                System.setErr(stderr);

                final MessagePayload payload =
                    mock(MessagePayload.class);

                final RuntimeException e = mock(RuntimeException.class);
                when(e.getMessage()).thenReturn("EXCEPTION MESSAGE");

                doThrow(e).when(delegate).send(payload);

                final NagiosNotifier notifier =
                    new NagiosNotifier(delegate);

                notifier.send(payload);

                verify(stderr).println("Unable to notify Nagios:");
                verify(stderr).println("EXCEPTION MESSAGE");
        }

        @Test
        public void shouldSendCriticalMessageOnFailure()
            throws Exception {
                final NagiosNotifier notifier =
                    new NagiosNotifier(delegate);

                final MessagePayload payload = notifier
                    .createMessagePayload("SERVICE NAME",
                        "SERVICE HOST", Level.CRITICAL,
                        "FAILURE MESSAGE");

                notifier.failed("SERVICE NAME", "SERVICE HOST",
                    "FAILURE MESSAGE");

                verify(delegate).send(payload);
        }

        @Test
        public void shouldSendUnknownMessageOnIgnored()
            throws Exception {
                final NagiosNotifier notifier =
                    new NagiosNotifier(delegate);

                final MessagePayload payload = notifier
                    .createMessagePayload("SERVICE NAME",
                        "SERVICE HOST", Level.UNKNOWN,
                        "IGNORED MESSAGE");

                notifier.ignored("SERVICE NAME", "SERVICE HOST",
                    "IGNORED MESSAGE");

                verify(delegate).send(payload);
        }

        @Test
        public void shouldSendOkMessageOnSuccess() throws Exception {
                final NagiosNotifier notifier =
                    new NagiosNotifier(delegate);

                final MessagePayload payload = notifier
                    .createMessagePayload("SERVICE NAME",
                        "SERVICE HOST", Level.OK, "SUCCESS MESSAGE");

                notifier.succeeded("SERVICE NAME", "SERVICE HOST",
                    "SUCCESS MESSAGE");

                verify(delegate).send(payload);
        }

        public static junit.framework.Test suite() {
                return new JUnit4TestAdapter(NagiosNotifierTests.class);
        }

        private NagiosSettings getSettings(
            final NagiosPassiveCheckSender notifier) throws Exception {
                return (NagiosSettings) valueOf(notifier,
                    "nagiosSettings");
        }

        private static <T> Object valueOf(final Object instance,
            final String fieldName) throws Exception {
                final Class type = instance.getClass();
                final Field field = type.getDeclaredField(fieldName);
                field.setAccessible(true);
                return field.get(instance);
        }
}
