#!/bin/bash
# This script takes as argument a single node
# address and attempts to connect to it
# $1 - the node's address

ping_test=$(timeout 1 ping $1 | grep ttl=)
if grep -q ttl= <<<$ping_test; then
	ssh -f -t -t k1333702@$1 "cd planning.domains; nohup java client/Client"
fi
