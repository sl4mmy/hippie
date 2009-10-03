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

import junit.framework.JUnit4TestAdapter;
import static org.junit.Assert.assertEquals;
import org.junit.Test;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

public class MonitorsServiceTests {
        @Test
        public void shouldHaveRuntimeRetentionPolicy() throws Exception {
                final Retention retention =
                    MonitorsService.class.getAnnotation(Retention.class);

                assertEquals(RetentionPolicy.RUNTIME, retention.value());
        }

        @Test
        public void shouldOnlyBeApplicableToMethods() throws Exception {
                final Target target =
                    MonitorsService.class.getAnnotation(Target.class);
                final ElementType[] elementTypes = target.value();

                assertEquals(1, elementTypes.length);
                assertEquals(ElementType.METHOD, elementTypes[0]);
        }

        @Test
        public void shouldDefaultFailureMessage() throws Exception {
                final MonitorsService annotation = withNameOnly();

                assertEquals("is not responding correctly.",
                    annotation.failureMessage());
        }

        @Test
        public void shouldDefaultIgnoredMessage() throws Exception {
                final MonitorsService annotation = withNameOnly();

                assertEquals("was ignored.", annotation.ignoredMessage());
        }

        @Test
        public void shouldDefaultSuccessMessage() throws Exception {
                final MonitorsService annotation = withNameOnly();

                assertEquals("is responding correctly.",
                    annotation.successMessage());
        }

        public static junit.framework.Test suite() {
                return new JUnit4TestAdapter(MonitorsServiceTests.class);
        }

        private MonitorsService annotation(final Class type,
            final String annotatedMethod) throws Exception {
                return type.getMethod(annotatedMethod)
                    .getAnnotation(MonitorsService.class);
        }

        private MonitorsService withNameOnly() throws Exception {
                class DoesNotMatter {
                        @MonitorsService(name = "DOES NOT MATTER")
                        public void doesNotMatter() {

                        }
                }

                return annotation(DoesNotMatter.class, "doesNotMatter");
        }
}
