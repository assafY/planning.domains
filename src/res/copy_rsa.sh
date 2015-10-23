#!/bin/bash
# This script should only be run once per system and user
# and was already run on the existing cluster list and user
# k1333702. Additionally before running script an RSA key
# needs to be created using command 'ssh-keygen -t rsa.

# Change username here
username="k1333702"

while IFS='' read -r line || [[ -n "$line" ]]; do
    (ssh-copy-id $username@$line)
done < "$1"
