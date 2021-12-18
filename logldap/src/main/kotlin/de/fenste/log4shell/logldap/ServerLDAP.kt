/*
 * Copyright (c) 2021.
 *
 * This file is part of scan-log4shell.
 *
 * scan-log4shell is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * scan-log4shell is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Foobar.  If not, see <https://www.gnu.org/licenses/>.
 */

package de.fenste.log4shell.logldap

import com.unboundid.ldap.listener.InMemoryDirectoryServer
import com.unboundid.ldap.listener.InMemoryDirectoryServerConfig
import com.unboundid.ldap.listener.InMemoryListenerConfig
import com.unboundid.ldap.listener.LDAPListenerClientConnection
import com.unboundid.ldap.listener.interceptor.InMemoryInterceptedSearchResult
import com.unboundid.ldap.listener.interceptor.InMemoryOperationInterceptor
import com.unboundid.ldap.sdk.LDAPException
import org.apache.logging.log4j.LogManager
import java.net.Inet4Address
import java.net.InetSocketAddress
import javax.net.ServerSocketFactory
import javax.net.SocketFactory
import javax.net.ssl.SSLSocketFactory

class ServerLDAP(private val port: Int) {

    private companion object {
        private val LOG = LogManager.getLogger(ServerLDAP::class.java)
        private const val LDAP_BASE = "dc=example,dc=com"
    }

    private val server: InMemoryDirectoryServer by lazy {
        val config = InMemoryDirectoryServerConfig(LDAP_BASE)
        config.listenerConfigs.add(
            InMemoryListenerConfig(
                "ipv4",
                Inet4Address.getByAddress(byteArrayOf(0, 0, 0, 0)),
                port,
                ServerSocketFactory.getDefault(),
                SocketFactory.getDefault(),
                SSLSocketFactory.getDefault() as SSLSocketFactory,
            )
        )
        config.addInMemoryOperationInterceptor(Interceptor())
        InMemoryDirectoryServer(config)
    }

    fun run() {
        LOG.info("Starting mock LDAP Server on 0.0.0.0:$port ...")

        try {
            server.startListening()
            LOG.info("Starup complete...")
        } catch (e: LDAPException) {
            LOG.error("Startup failed.", e)
        }
    }

    inner class Interceptor : InMemoryOperationInterceptor() {

        private val field by lazy {
            val field = Class.forName("com.unboundid.ldap.listener.interceptor.InterceptedOperation")
                .getDeclaredField("clientConnection")
            field.isAccessible = true
            field
        }

        override fun processSearchResult(result: InMemoryInterceptedSearchResult?) {
            val conn = field.get(result) as LDAPListenerClientConnection
            val remoteAddr = conn.socket.remoteSocketAddress as InetSocketAddress

            val ip = remoteAddr.address
            val port = remoteAddr.port

            LOG.warn("Connection from $ip:$port. Is it affected by the cve?")

            super.processSearchResult(result)
        }
    }
}
