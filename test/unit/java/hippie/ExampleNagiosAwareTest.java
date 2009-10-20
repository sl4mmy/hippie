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

import static org.junit.Assert.fail;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(NagiosAwareRunner.class)
public class ExampleNagiosAwareTest {
        static {
                System.setProperty("hippie.nagios.server", "localhost");
                System
                    .setProperty("hippie.nagios.password", "password");
        }

        @Test
        @MonitorsService(name = "foo/bar(baz)", onHost = "localhost")
        public void foo() {
                fail("nagios sucks");
        }

        @Test
        @MonitorsService(name = "hello!!!!!!", onHost = "localhost")
        public void bar() {
        }
}
