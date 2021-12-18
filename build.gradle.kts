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

plugins {
    kotlin("jvm") version "1.6.10" apply false
    id("com.github.johnrengelman.shadow") version "7.1.1" apply false
    id("io.gitlab.arturbosch.detekt") version "1.19.0" apply false
    id("org.jlleitschuh.gradle.ktlint") version "10.2.0" apply false
    id("org.jetbrains.dokka") version "1.6.0" apply false
}

group = "de.fenste.log4shell"
version = "0.1"
