#!/bin/bash

#
# Copyright (C) 2022  AniTrend
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

target_file=koin.properties
target_directory=./app-core/src/main/resources/org/koin/core

target_path=$target_directory/$target_file

if [ -f $target_path ]; then
  echo "'$target_path' already exists, skipping file creation"; exit 1;
else
  mkdir -vp $target_directory

  touch $target_path
  {
    echo "aniListClientId=aniListClientId"
    echo "aniListClientSecret=aniListClientSecret"
    echo "traktClientId=traktClientId"
    echo "traktClientSecret=traktClientSecret"
    echo "tmdbClientSecret=tmdbClientSecret"
  } >> $target_path

  echo "Make sure to update the contents of '$target_path' according to the setup docs"
fi