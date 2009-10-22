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

import hippie.notifiers.NagiosNotifier;
import junit.framework.JUnit4TestAdapter;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;
import org.junit.rules.TestWatchman;
import org.junit.runners.model.FrameworkMethod;
import org.mockito.Mock;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;

public class ServiceMonitorTests {
        @Mock
        private Exception cause;

        @Mock
        private FrameworkMethod method;

        @Mock
        private NagiosNotifier notifier;

        @Before
        public void initializeMocks() throws Exception {
                MockitoAnnotations.initMocks(this);
        }

        @Test
        public void shouldBeAnInstanceOfWatchman() throws Exception {
                assertEquals(true, new ServiceMonitor("SERVICE HOST",
                    notifier) instanceof TestWatchman);
        }

        @Test
        public void shouldNotifyNagiosWhenServiceMonitoringTestsFail()
            throws Exception {
                final MonitorsService annotation =
                    mock(MonitorsService.class);
                when(annotation.name()).thenReturn("SERVICE NAME");
                when(annotation.onHost()).thenReturn("SERVICE HOST");
                when(annotation.failureMessage())
                    .thenReturn("FAILURE MESSAGE");
                when(method.getAnnotation(MonitorsService.class))
                    .thenReturn(annotation);

                final ServiceMonitor watchman =
                    new ServiceMonitor("SERVICE HOST", notifier);

                watchman.failed(cause, method);

                verify(notifier).failed("SERVICE NAME", "SERVICE HOST",
                    "FAILURE MESSAGE");
        }

        @Test
        public void shouldNotNotifyNagiosWhenNonServiceMonitoringTestsFail()
            throws Exception {
                when(method.getAnnotation(MonitorsService.class))
                    .thenReturn(null);

                final ServiceMonitor watchman =
                    new ServiceMonitor("SERVICE HOST", notifier);

                watchman.failed(cause, method);

                verifyZeroInteractions(notifier);
        }

        @Test
        public void shouldChooseServiceHostNameFromAnnotationWhenSetOnAnnotationAndServiceMonitoringTestsFail()
            throws Exception {
                final MonitorsService annotation =
                    mock(MonitorsService.class);
                when(annotation.name()).thenReturn("SERVICE NAME");
                when(annotation.onHost())
                    .thenReturn("SERVICE HOST NAME FROM ANNOTATION");
                when(annotation.failureMessage())
                    .thenReturn("FAILURE MESSAGE");
                when(method.getAnnotation(MonitorsService.class))
                    .thenReturn(annotation);

                final ServiceMonitor watchman = new ServiceMonitor(
                    "SERVICE HOST NAME FROM CONSTRUCTOR", notifier);

                watchman.failed(cause, method);

                verify(notifier).failed("SERVICE NAME",
                    "SERVICE HOST NAME FROM ANNOTATION",
                    "FAILURE MESSAGE");
        }

        @Test
        public void shouldChooseServiceHostNameFromConstructorWhenServiceHostNameIsNotSetOnAnnotationAndServiceMonitoringTestsFail()
            throws Exception {
                final MonitorsService annotation =
                    mock(MonitorsService.class);
                when(annotation.name()).thenReturn("SERVICE NAME");
                when(annotation.failureMessage())
                    .thenReturn("FAILURE MESSAGE");
                when(method.getAnnotation(MonitorsService.class))
                    .thenReturn(annotation);

                final ServiceMonitor watchman = new ServiceMonitor(
                    "SERVICE HOST NAME FROM CONSTRUCTOR", notifier);

                watchman.failed(cause, method);

                verify(notifier).failed("SERVICE NAME",
                    "SERVICE HOST NAME FROM CONSTRUCTOR",
                    "FAILURE MESSAGE");
        }

        @Test
        public void shouldChooseServiceHostNameFromConstructorWhenServiceHostNameIsEmptyOnAnnotationAndServiceMonitoringTestsFail()
            throws Exception {
                final MonitorsService annotation =
                    mock(MonitorsService.class);
                when(annotation.name()).thenReturn("SERVICE NAME");
                when(annotation.onHost()).thenReturn("");
                when(annotation.failureMessage())
                    .thenReturn("FAILURE MESSAGE");
                when(method.getAnnotation(MonitorsService.class))
                    .thenReturn(annotation);

                final ServiceMonitor watchman = new ServiceMonitor(
                    "SERVICE HOST NAME FROM CONSTRUCTOR", notifier);

                watchman.failed(cause, method);

                verify(notifier).failed("SERVICE NAME",
                    "SERVICE HOST NAME FROM CONSTRUCTOR",
                    "FAILURE MESSAGE");
        }

        @Test
        public void shouldChooseServiceHostNameFromConstructorWhenServiceHostNameIsBlankOnAnnotationAndServiceMonitoringTestsFail()
            throws Exception {
                final MonitorsService annotation =
                    mock(MonitorsService.class);
                when(annotation.name()).thenReturn("SERVICE NAME");
                when(annotation.onHost()).thenReturn(" \t\r\n");
                when(annotation.failureMessage())
                    .thenReturn("FAILURE MESSAGE");
                when(method.getAnnotation(MonitorsService.class))
                    .thenReturn(annotation);

                final ServiceMonitor watchman = new ServiceMonitor(
                    "SERVICE HOST NAME FROM CONSTRUCTOR", notifier);

                watchman.failed(cause, method);

                verify(notifier).failed("SERVICE NAME",
                    "SERVICE HOST NAME FROM CONSTRUCTOR",
                    "FAILURE MESSAGE");
        }

        @Test
        public void shouldDefaultServiceHostNameToLocalHostNameWhenServiceHostNameIsNotExplicitlySetAndServiceMonitoringTestsFail()
            throws Exception {
                final MonitorsService annotation =
                    mock(MonitorsService.class);
                when(annotation.name()).thenReturn("SERVICE NAME");
                when(annotation.failureMessage())
                    .thenReturn("FAILURE MESSAGE");
                when(method.getAnnotation(MonitorsService.class))
                    .thenReturn(annotation);

                final ServiceMonitor watchman =
                    new ServiceMonitor(notifier);

                watchman.failed(cause, method);

                verify(notifier).failed("SERVICE NAME", "localhost",
                    "FAILURE MESSAGE");
        }

        @Test
        public void shouldNotifyNagiosWhenServiceMonitoringTestsSucceed()
            throws Exception {
                final MonitorsService annotation =
                    mock(MonitorsService.class);
                when(annotation.name()).thenReturn("SERVICE NAME");
                when(annotation.onHost()).thenReturn("SERVICE HOST");
                when(annotation.successMessage())
                    .thenReturn("SUCCESS MESSAGE");
                when(method.getAnnotation(MonitorsService.class))
                    .thenReturn(annotation);

                final ServiceMonitor watchman =
                    new ServiceMonitor("SERVICE HOST", notifier);

                watchman.succeeded(method);

                verify(notifier)
                    .succeeded("SERVICE NAME", "SERVICE HOST",
                        "SUCCESS MESSAGE");
        }

        @Test
        public void shouldNotNotifyNagiosWhenNonServiceMonitoringTestsSucceed()
            throws Exception {
                when(method.getAnnotation(MonitorsService.class))
                    .thenReturn(null);

                final ServiceMonitor watchman =
                    new ServiceMonitor("SERVICE HOST", notifier);

                watchman.succeeded(method);

                verifyZeroInteractions(notifier);
        }

        @Test
        public void shouldChooseServiceHostNameFromAnnotationWhenSetOnAnnotationAndServiceMonitoringTestsSucceed()
            throws Exception {
                final MonitorsService annotation =
                    mock(MonitorsService.class);
                when(annotation.name()).thenReturn("SERVICE NAME");
                when(annotation.onHost())
                    .thenReturn("SERVICE HOST NAME FROM ANNOTATION");
                when(annotation.successMessage())
                    .thenReturn("SUCCESS MESSAGE");
                when(method.getAnnotation(MonitorsService.class))
                    .thenReturn(annotation);

                final ServiceMonitor watchman = new ServiceMonitor(
                    "SERVICE HOST NAME FROM CONSTRUCTOR", notifier);

                watchman.succeeded(method);

                verify(notifier).succeeded("SERVICE NAME",
                    "SERVICE HOST NAME FROM ANNOTATION",
                    "SUCCESS MESSAGE");
        }

        @Test
        public void shouldChooseServiceHostNameFromConstructorWhenServiceHostNameIsNotSetOnAnnotationAndServiceMonitoringTestsSucceed()
            throws Exception {
                final MonitorsService annotation =
                    mock(MonitorsService.class);
                when(annotation.name()).thenReturn("SERVICE NAME");
                when(annotation.successMessage())
                    .thenReturn("SUCCESS MESSAGE");
                when(method.getAnnotation(MonitorsService.class))
                    .thenReturn(annotation);

                final ServiceMonitor watchman = new ServiceMonitor(
                    "SERVICE HOST NAME FROM CONSTRUCTOR", notifier);

                watchman.succeeded(method);

                verify(notifier).succeeded("SERVICE NAME",
                    "SERVICE HOST NAME FROM CONSTRUCTOR",
                    "SUCCESS MESSAGE");
        }

        @Test
        public void shouldChooseServiceHostNameFromConstructorWhenServiceHostNameIsEmptyOnAnnotationAndServiceMonitoringTestsSucceed()
            throws Exception {
                final MonitorsService annotation =
                    mock(MonitorsService.class);
                when(annotation.name()).thenReturn("SERVICE NAME");
                when(annotation.onHost()).thenReturn("");
                when(annotation.successMessage())
                    .thenReturn("SUCCESS MESSAGE");
                when(method.getAnnotation(MonitorsService.class))
                    .thenReturn(annotation);

                final ServiceMonitor watchman = new ServiceMonitor(
                    "SERVICE HOST NAME FROM CONSTRUCTOR", notifier);

                watchman.succeeded(method);

                verify(notifier).succeeded("SERVICE NAME",
                    "SERVICE HOST NAME FROM CONSTRUCTOR",
                    "SUCCESS MESSAGE");
        }

        @Test
        public void shouldChooseServiceHostNameFromConstructorWhenServiceHostNameIsBlankOnAnnotationAndServiceMonitoringTestsSucceed()
            throws Exception {
                final MonitorsService annotation =
                    mock(MonitorsService.class);
                when(annotation.name()).thenReturn("SERVICE NAME");
                when(annotation.onHost()).thenReturn(" \t\r\n");
                when(annotation.successMessage())
                    .thenReturn("SUCCESS MESSAGE");
                when(method.getAnnotation(MonitorsService.class))
                    .thenReturn(annotation);

                final ServiceMonitor watchman = new ServiceMonitor(
                    "SERVICE HOST NAME FROM CONSTRUCTOR", notifier);

                watchman.succeeded(method);

                verify(notifier).succeeded("SERVICE NAME",
                    "SERVICE HOST NAME FROM CONSTRUCTOR",
                    "SUCCESS MESSAGE");
        }

        @Test
        public void shouldDefaultServiceHostNameToLocalHostNameWhenServiceHostNameIsNotExplicitlySetAndServiceMonitoringTestsSucceed()
            throws Exception {
                final MonitorsService annotation =
                    mock(MonitorsService.class);
                when(annotation.name()).thenReturn("SERVICE NAME");
                when(annotation.successMessage())
                    .thenReturn("SUCCESS MESSAGE");
                when(method.getAnnotation(MonitorsService.class))
                    .thenReturn(annotation);

                final ServiceMonitor watchman =
                    new ServiceMonitor(notifier);

                watchman.succeeded(method);

                verify(notifier).succeeded("SERVICE NAME", "localhost",
                    "SUCCESS MESSAGE");
        }

        public static junit.framework.Test suite() {
                return new JUnit4TestAdapter(ServiceMonitorTests.class);
        }
}
