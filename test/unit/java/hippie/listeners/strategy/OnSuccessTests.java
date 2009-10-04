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

public class OnSuccessTests {
        @Mock
        private MonitorsService annotation;

        @Mock
        private NagiosNotifier notifier;

        @Before
        public void initializeMocks() throws Exception {
                MockitoAnnotations.initMocks(this);
        }

        @Test
        public void shouldBeAnInstanceofStrategy() throws Exception {
                assertEquals(true,
                    new OnSuccess(notifier) instanceof Strategy);
        }

        @Test
        public void shouldGetServiceNameFromAnnotationWhenExecuting()
            throws Exception {
                final OnSuccess strategy = new OnSuccess(notifier);

                strategy.execute(annotation);

                verify(annotation).name();
        }

        @Test
        public void shouldGetSuccessMessageFromAnnotationWhenExecuting()
            throws Exception {
                final OnSuccess strategy = new OnSuccess(notifier);

                strategy.execute(annotation);

                verify(annotation).successMessage();
        }

        @Test
        public void shouldNotifyNagiosOfSuccessfulServiceCheckWhenExecuting()
            throws Exception {
                when(annotation.name()).thenReturn("SERVICE NAME");
                when(annotation.successMessage())
                    .thenReturn("SUCCESS MESSAGE");

                final OnSuccess strategy = new OnSuccess(notifier);

                strategy.execute(annotation);

                verify(notifier)
                    .succeeded("SERVICE NAME", "SUCCESS MESSAGE");
        }

        @Test
        public void shouldReturnAnInstanceOfOnFailureWhenGettingFailureStrategy()
            throws Exception {
                final OnSuccess strategy = new OnSuccess(notifier);

                assertEquals(true,
                    strategy.failed() instanceof OnFailure);
        }

        @Test
        public void shouldReturnAnInstanceOfOnIgnoredWhenGettingIgnoredStrategy()
            throws Exception {
                final OnSuccess strategy = new OnSuccess(notifier);

                assertEquals(true,
                    strategy.ignored() instanceof OnIgnored);
        }

        @Test
        public void shouldReturnSelfWhenGettingSuccessStrategy()
            throws Exception {
                final OnSuccess strategy = new OnSuccess(notifier);

                assertEquals(strategy, strategy.succeeded());
        }

        public static junit.framework.Test suite() {
                return new JUnit4TestAdapter(OnSuccessTests.class);
        }
}
