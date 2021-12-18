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

import kotlinx.cli.ArgParser
import kotlinx.cli.ArgType
import kotlinx.cli.default

private const val DEFAULT_PORT = 1389

fun main(args: Array<String>) {

    val parser = ArgParser("server-exploit")
    val port by parser.option(
        ArgType.Int,
        fullName = "port",
        shortName = "p",
        description = "Port the server listens to",
    ).default(DEFAULT_PORT)
    parser.parse(args)

    val server = ServerLDAP(port)
    server.run()
}
