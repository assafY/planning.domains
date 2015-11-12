#!/bin/bash
# This client side script copies all result files
# created by a planner for a problem file and copies
# them to the server. The script is used because Java's
# ProcessBuilder reads the bash * argument literally.
#
# $1 - name of the result file prefix
# $2 - username, server address, remote directory

scp $1.* $2
