#!/bin/bash

function create_directories {
    mkdir -p ./app-core/src/main/resources/org/koin/core/
    cd ./app-core/src/main/resources/org/koin/core/
}

function create_files {
    touch koin.properties
    echo "aniListClientId=\"aniListClientId\"" >> koin.properties
    echo "aniListClientSecret=\"aniListClientSecret\"" >> koin.properties
    echo "traktClientId=\"traktClientId\"" >> koin.properties
    echo "traktClientSecret=\"traktClientSecret\"" >> koin.properties
    echo "tmdbClientSecret=\"tmdbClientSecret\"" >> koin.properties
}

create_directories
create_files
