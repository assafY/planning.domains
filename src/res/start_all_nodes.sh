#!/bin/bash
# This script reads the cluster list text file and loops through
# the clients in the list. For each one it checks whether it is
# up and connected, and if yes ssh's to that cluster and runs the
# client side Java file.

while IFS='' read -r line || [[ -n "$line" ]]; do
	ping_test=$(timeout 1 ping $line | grep ttl=)
	if grep -q ttl= <<<$ping_test; then
		ssh -f -t -t k1333702@$line "cd planning.domains; nohup java client/Client"
	fi
done < "$1"
