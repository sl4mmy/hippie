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

import hippie.notifiers.NagiosNotifier;
import hippie.MonitorsService;

/**
 * Understands how to notify Nagios when service-monitoring test cases
 * pass.
 */
public class OnSuccess implements Strategy {
        private final NagiosNotifier notifier;

        public OnSuccess(final NagiosNotifier notifier) {
                this.notifier = notifier;
        }

        public void execute(final String name, final String message) {
        }

        public String getMessage(final MonitorsService annotation) {
                return null;
        }

        public OnFailure failed() {
                return null;
        }

        public OnIgnored ignored() {
                return null;
        }

        public OnSuccess succeeded() {
                return null;
        }
}
