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
import org.junit.rules.TestWatchman;
import org.junit.runners.model.FrameworkMethod;

/**
 * Understands how to automatically notify Nagios servers of the status
 * of external service endpoints.
 */
public class ServiceMonitor extends TestWatchman {
        private final NagiosNotifier notifier;

        public ServiceMonitor(final NagiosNotifier notifier) {
                this.notifier = notifier;
        }

        @Override
        public void succeeded(final FrameworkMethod method) {
                final MonitorsService annotation =
                    method.getAnnotation(MonitorsService.class);

                if (annotation != null) {
                        final String serviceName = annotation.name();
                        final String serviceHost = annotation.onHost();
                        notifier.succeeded(serviceName, serviceHost,
                            annotation.successMessage());
                }
        }

        @Override
        public void failed(final Throwable e,
            final FrameworkMethod method) {
                final MonitorsService annotation =
                    method.getAnnotation(MonitorsService.class);

                if (annotation != null) {
                        final String serviceName = annotation.name();
                        final String serviceHost = annotation.onHost();
                        notifier.failed(serviceName, serviceHost,
                            annotation.failureMessage());
                }
        }
}
