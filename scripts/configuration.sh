#!/bin/bash

function create_directories {
    mkdir -p ../data/config/
    cd ../data/
}

function rename_directory {
    mv config .config
    cd .config
}

function create_files {
    touch .secrets.properties
    echo "cliendId=\"cliend_id\"" >> .secrets.properties
    echo "clientSecret=\"client_secret\"" >> .secrets.properties
}


create_directories
rename_directory
create_files

