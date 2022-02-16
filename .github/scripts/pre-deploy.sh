#!/bin/bash

#
# Copyright (C) 2021  AniTrend
#
#     This program is free software: you can redistribute it and/or modify
#     it under the terms of the GNU General Public License as published by
#     the Free Software Foundation, either version 3 of the License, or
#     (at your option) any later version.
#
#     This program is distributed in the hope that it will be useful,
#     but WITHOUT ANY WARRANTY; without even the implied warranty of
#     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
#     GNU General Public License for more details.
#
#     You should have received a copy of the GNU General Public License
#     along with this program.  If not, see <https://www.gnu.org/licenses/>.
#

function  create_files {
    echo "$KEYSTORE" > anitrend-keystore.asc
    gpg -d --passphrase "$KEYSTORE_PASSPHRASE" --batch anitrend-keystore.asc > app/anitrend-keystore.jks
    echo "$KEYSTORE_PROPERTIES" > keystore.properties.asc
    gpg -d --passphrase "$KEYSTORE_PROPERTIES_PASSPHRASE" --batch keystore.properties.asc > app/.config/keystore.properties
    echo "$KOIN_PROPERTIES" > koin.properties.asc
    gpg -d --passphrase "$KOIN_PROPERTIES_PASSPHRASE" --batch koin.properties.asc > app-core/src/main/resources/org/koin/core/koin.properties
    echo "$GOOGLE_SERVICES" > google-services.json.asc
    gpg -d --passphrase "$GOOGLE_SERVICES_PASSPHRASE" --batch google-services.json.asc > app/google-services.json
}

create_files