#!/bin/bash

function create_directories {
    mkdir -p ./app-data/.config/
    cd ./app-data/.config/
}

function create_files {
    touch secrets.properties
    echo "clientId=\"client_id\"" >> secrets.properties
    echo "clientSecret=\"client_secret\"" >> secrets.properties
}


create_directories
create_files

