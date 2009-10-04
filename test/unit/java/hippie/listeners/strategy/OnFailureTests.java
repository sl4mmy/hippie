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
package hippie.listeners.strategy;

import hippie.MonitorsService;
import hippie.notifiers.NagiosNotifier;
import junit.framework.JUnit4TestAdapter;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;

public class OnFailureTests {
        @Mock
        private MonitorsService annotation;

        @Mock
        private NagiosNotifier notifier;

        @Before
        public void initializeMocks() throws Exception {
                MockitoAnnotations.initMocks(this);
        }

        @Test
        public void shouldBeAnInstanceOfStrategy() throws Exception {
                assertEquals(true,
                    new OnFailure(notifier) instanceof Strategy);
        }

        @Test
        public void shouldGetServiceNameFromAnnotationWhenExecuting()
            throws Exception {
                final OnFailure strategy = new OnFailure(notifier);

                strategy.execute(annotation);

                verify(annotation).name();
        }

        @Test
        public void shouldGetFailureMessageFromAnnotationWhenExecuting()
            throws Exception {
                final OnFailure strategy = new OnFailure(notifier);

                strategy.execute(annotation);

                verify(annotation).failureMessage();
        }

        @Test
        public void shouldNotifyNagiosOfFailingServiceCheckWhenExecuting()
            throws Exception {
                when(annotation.name()).thenReturn("SERVICE NAME");
                when(annotation.failureMessage())
                    .thenReturn("FAILURE MESSAGE");

                final OnFailure strategy = new OnFailure(notifier);

                strategy.execute(annotation);

                verify(notifier)
                    .failed("SERVICE NAME", "FAILURE MESSAGE");
        }

        @Test
        public void shouldReturnSelfWhenGettingFailureStrategy()
            throws Exception {
                final OnFailure strategy = new OnFailure(notifier);

                assertEquals(strategy, strategy.failed());
        }

        @Test
        public void shouldReturnAnInstanceOfOnIgnoredWhenGettingIgnoredStrategy()
            throws Exception {
                final OnFailure strategy = new OnFailure(notifier);

                assertEquals(true,
                    strategy.ignored() instanceof OnIgnored);
        }

        @Test
        public void shouldReturnAnInstanceOfOnSuccessWhenGettingSuccessStrategy()
            throws Exception {
                final OnFailure strategy = new OnFailure(notifier);

                assertEquals(true,
                    strategy.succeeded() instanceof OnSuccess);
        }

        public static junit.framework.Test suite() {
                return new JUnit4TestAdapter(OnFailureTests.class);
        }
}
