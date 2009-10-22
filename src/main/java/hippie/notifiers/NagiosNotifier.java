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

/**
 * Understands how to send NSCA passive service checks.
 */
public class NagiosNotifier {
        private final NagiosPassiveCheckSender delegate;

        public NagiosNotifier(final String nscaServer,
            final String nscaPassword, final int nscaPort,
            final int connectionTimeout, final int responseTimeout) {
                this(new NagiosSettings() {
                        {
                                setNagiosHost(nscaServer);
                                setPassword(nscaPassword);
                                setPort(nscaPort);
                                setConnectTimeout(connectionTimeout);
                                setTimeout(responseTimeout);
                        }
                });
        }

        public NagiosNotifier(final NagiosSettings settings) {
                this(new NagiosPassiveCheckSender(settings));
        }

        public NagiosNotifier(final NagiosPassiveCheckSender delegate) {
                this.delegate = delegate;
        }

        public void failed(final String serviceName,
            final String serviceHost, final String message) {
                send(createMessagePayload(serviceName, serviceHost,
                    Level.CRITICAL, message));
        }

        public void ignored(final String serviceName,
            final String serviceHost, final String message) {
                send(createMessagePayload(serviceName, serviceHost,
                    Level.UNKNOWN, message));
        }

        public void succeeded(final String serviceName,
            final String serviceHost, final String message) {
                send(createMessagePayload(serviceName, serviceHost,
                    Level.OK, message));
        }

        public void send(final MessagePayload notification) {
                try {
                        delegate.send(notification);
                } catch (Exception e) {
                        System.err.println("Unable to notify Nagios:");
                        System.err.println(e.getMessage());
                }
        }

        public MessagePayload createMessagePayload(
            final String serviceName, final String serviceHost,
            final Level level, final String message) {
                return new MessagePayload() {
                        {
                                setServiceName(serviceName);
                                setHostname(serviceHost);
                                setLevel(level);
                                setMessage(message);
                        }
                };
        }

        NagiosPassiveCheckSender getDelegate() {
                return delegate;
        }
}
