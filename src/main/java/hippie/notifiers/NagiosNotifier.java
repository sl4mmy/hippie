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

import java.net.InetAddress;

/**
 * Understands how to send NSCA passive service checks.
 */
public class NagiosNotifier {
        private final String localhost;

        private final NagiosPassiveCheckSender notifier;

        public NagiosNotifier(final String host, final int port,
            final int connectionTimeout, final int responseTimeout,
            final String password) {
                final NagiosSettings settings =
                    NagiosSettingsBuilder.withNagiosHost(host)
                        .withPort(port)
                        .withConnectionTimeout(connectionTimeout)
                        .withResponseTimeout(responseTimeout)
                        .withPassword(password).create();
                this.localhost = getCanonicalLocalHostName();
                this.notifier = new NagiosPassiveCheckSender(settings);
        }

        public void failed(final String serviceName,
            final String message) {
                final MessagePayload notification =
                    MessagePayloadBuilder.withHostname(localhost)
                        .withLevel(Level.CRITICAL)
                        .withServiceName(serviceName)
                        .withMessage(message).create();
                send(notification);
        }

        public void ignored(final String serviceName,
            final String message) {
                final MessagePayload notification =
                    MessagePayloadBuilder.withHostname(localhost)
                        .withLevel(Level.UNKNOWN)
                        .withServiceName(serviceName)
                        .withMessage(message).create();
                send(notification);
        }

        public void succeeded(final String serviceName,
            final String message) {
                final MessagePayload notification =
                    MessagePayloadBuilder.withHostname(localhost)
                        .withLevel(Level.OK)
                        .withServiceName(serviceName)
                        .withMessage(message).create();
                send(notification);
        }

        private String getCanonicalLocalHostName() {
                try {
                        return InetAddress.getLocalHost()
                            .getCanonicalHostName();
                } catch (Exception e) {
                        return "localhost";
                }
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
