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
import com.googlecode.jsendnsca.core.builders.MessagePayloadBuilder;
import com.googlecode.jsendnsca.core.builders.NagiosSettingsBuilder;

/**
 * Understands how to send NSCA passive service checks.
 */
public class NagiosNotifier {
        private final NagiosPassiveCheckSender notifier;

        public NagiosNotifier(final String nscaServer,
            final String nscaPassword, final int nscaPort,
            final int connectionTimeout, final int responseTimeout) {
                final NagiosSettings settings =
                    NagiosSettingsBuilder.withNagiosHost(nscaServer)
                        .withPassword(nscaPassword).withPort(nscaPort)
                        .withConnectionTimeout(connectionTimeout)
                        .withResponseTimeout(responseTimeout).create();
                this.notifier = new NagiosPassiveCheckSender(settings);
        }

        public void failed(final String serviceName,
            final String serviceHost, final String message) {
                final MessagePayload notification =
                    MessagePayloadBuilder.withHostname(serviceHost)
                        .withLevel(Level.CRITICAL)
                        .withServiceName(serviceName)
                        .withMessage(message).create();
                send(notification);
        }

        public void ignored(final String serviceName,
            final String serviceHost, final String message) {
                final MessagePayload notification =
                    MessagePayloadBuilder.withHostname(serviceHost)
                        .withLevel(Level.UNKNOWN)
                        .withServiceName(serviceName)
                        .withMessage(message).create();
                send(notification);
        }

        public void succeeded(final String serviceName,
            final String serviceHost, final String message) {
                final MessagePayload notification =
                    MessagePayloadBuilder.withHostname(serviceHost)
                        .withLevel(Level.OK)
                        .withServiceName(serviceName)
                        .withMessage(message).create();
                send(notification);
        }

        private void send(final MessagePayload notification) {
                try {
                        notifier.send(notification);
                } catch (Exception e) {
                        System.err.println("Unable to notify Nagios:");
                        System.err.println(e.getMessage());
                }
        }
}
