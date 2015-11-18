#!/bin/bash
# This script runs the plan validator on all result files
# returned from a node. The results are echoed and the best
# result is assigned to a problem object by the server
# $1 - path to validation script
# $2 - path to domain file
# $3 - path to problem files
# $4 - path to result files
$1validate $2 $3 $4.* | grep 'Value'


