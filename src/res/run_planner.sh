#!/bin/bash
# This client side script runs a planner on a
# problem file saving all output to a log file
#
# $1 path to planner
# $2 path to domain file
# $3 path to problem file
# $4 path to result and log files

$1/plan $2 $3 $4 >> $4_log

