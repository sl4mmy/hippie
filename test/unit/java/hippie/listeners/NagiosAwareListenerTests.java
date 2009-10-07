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
import hippie.notifiers.NagiosNotifier;
import junit.framework.JUnit4TestAdapter;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.Description;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunListener;
import org.mockito.Mock;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;

public class NagiosAwareListenerTests {
        @Mock
        private Description description;

        @Mock
        private Failure failure;

        @Mock
        private NagiosNotifier notifier;

        @Mock
        private OnSuccess strategy;

        @Before
        public void initializeMocks() throws Exception {
                MockitoAnnotations.initMocks(this);
        }

        @Test
        public void shouldBeAnInstanceOfNagiosAwareListener()
            throws Exception {
                final NagiosAwareListener listener =
                    new NagiosAwareListener(notifier, strategy);

                assertEquals(true, listener instanceof RunListener);
        }

        @Test
        public void shouldResetToSuccessStrategyWhenStartingEachTest()
            throws Exception {
                final NagiosAwareListener listener =
                    new NagiosAwareListener(notifier, strategy);

                listener.testStarted(description);

                verify(strategy).success();
        }

        @Test
        public void shouldGetMonitorsServiceAnnotationFromDescriptionWhenEachTestFinishes()
            throws Exception {
                final NagiosAwareListener listener =
                    new NagiosAwareListener(notifier, strategy);

                listener.testFinished(description);

                verify(description)
                    .getAnnotation(MonitorsService.class);
        }

        @Test
        public void shouldExecuteStrategyWhenEachTestFinishesAndMonitorsServiceAnnotationIsNotNull()
            throws Exception {
                final MonitorsService annotation =
                    mock(MonitorsService.class);
                when(description.getAnnotation(MonitorsService.class))
                    .thenReturn(annotation);

                final NagiosAwareListener listener =
                    new NagiosAwareListener(notifier, strategy);

                listener.testFinished(description);

                verify(strategy).execute(annotation);
        }

        @Test
        public void shouldNotExecuteStrategyWhenAnyTestFinishesAndMonitorsServiceAnnotationIsNull()
            throws Exception {
                final NagiosAwareListener listener =
                    new NagiosAwareListener(notifier, strategy);

                listener.testFinished(description);

                verifyZeroInteractions(strategy);
        }

        @Test
        public void shouldUseFailureStrategyWhenTestsFail()
            throws Exception {
                final NagiosAwareListener listener =
                    new NagiosAwareListener(notifier, strategy);

                listener.testFailure(failure);

                verify(strategy).failed();
        }

        @Test
        public void shouldUseFailureStrategyWhenTestAssumptionsFail()
            throws Exception {
                final NagiosAwareListener listener =
                    new NagiosAwareListener(notifier, strategy);

                listener.testAssumptionFailure(failure);

                verify(strategy).failed();
        }

        @Test
        public void shouldUseIgnoredStrategyWhenTestsAreIgnored()
            throws Exception {
                final NagiosAwareListener listener =
                    new NagiosAwareListener(notifier, strategy);

                listener.testIgnored(description);

                verify(strategy).ignored();
        }

        public static junit.framework.Test suite() {
                return new JUnit4TestAdapter(
                    NagiosAwareListenerTests.class);
        }
}
