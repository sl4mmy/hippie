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
package hippie.configurations;

import java.util.Properties;

/**
 * Understands how the user wants hippie to behave.
 */
public class Configuration {
        private final int connectionTimeout;

        private final String nagiosPassword;

        private final int nagiosPort;

        private final String nagiosServer;

        private final int responseTimeout;

        public Configuration(final Properties properties) {
                nagiosServer =
                    properties.getProperty("hippie.nsca.server");
                nagiosPassword =
                    properties.getProperty("hippie.nsca.password");
                nagiosPort = toInt(
                    properties.getProperty("hippie.nsca.port", "5667"));
                connectionTimeout = toInt(properties.getProperty(
                    "hippie.nsca.timeout.connection", "5000"));
                responseTimeout = toInt(properties.getProperty(
                    "hippie.nsca.timeout.response", "15000"));
        }

        public String getNagiosServer() {
                if (nagiosServer == null) {
                        bombOnNull("hippie.nsca.server");
                } else if (isEmpty(nagiosServer)) {
                        bombOnEmpty("hippie.nsca.server");
                }

                return nagiosServer;
        }

        public String getNagiosPassword() {
                if (nagiosPassword == null) {
                        bombOnNull("hippie.nsca.password");
                } else if (isEmpty(nagiosPassword)) {
                        bombOnEmpty("hippie.nsca.password");
                }

                return nagiosPassword;
        }

        public int getNagiosPort() {
                return nagiosPort;
        }

        public int getConnectionTimeout() {
                return connectionTimeout;
        }

        public int getResponseTimeout() {
                return responseTimeout;
        }

        private boolean isEmpty(final String value) {
                return "".equals(value.trim());
        }

        private int toInt(final String value) {
                return Integer.valueOf(value);
        }

        private void bombOnEmpty(final String property) {
                bomb(property, "empty");
        }

        private void bombOnNull(final String property) {
                bomb(property, "not set");
        }

        private void bomb(final String property, final String problem) {
                throw new IllegalStateException(
                    "The property " + property + " is " + problem);
        }
}
