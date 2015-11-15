#!/bin/bash
# This client side script deletes all result files for
# a problem after it is confirmed the files were sent to server.
# $1 name of result file(s)

rm $1.* && rm $1_log
