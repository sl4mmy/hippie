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
        private final String serviceHost;

        private final NagiosNotifier notifier;

        public ServiceMonitor(final NagiosNotifier notifier) {
                this("localhost", notifier);
        }

        public ServiceMonitor(final String serviceHost,
            final NagiosNotifier notifier) {
                this.serviceHost = serviceHost;
                this.notifier = notifier;
        }

        @Override
        public void succeeded(final FrameworkMethod method) {
                final MonitorsService annotation =
                    method.getAnnotation(MonitorsService.class);

                if (annotation != null) {
                        final String serviceName = annotation.name();
                        final String hostName =
                            chooseServiceHostName(annotation.onHost());
                        notifier.succeeded(serviceName, hostName,
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
                        final String hostName =
                            chooseServiceHostName(annotation.onHost());
                        notifier.failed(serviceName, hostName,
                            annotation.failureMessage());
                }
        }

        private String chooseServiceHostName(final String hostName) {
                if (isSet(hostName)) {
                        return hostName;
                }

                return serviceHost;
        }

        private boolean isSet(final String value) {
                return !(value == null || "".equals(value.trim()));
        }
}
