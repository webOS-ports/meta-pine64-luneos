#!/bin/sh 

# Keep reloading the touchscreen driver until the probe is successful

while ! [ -d /sys/devices/platform/soc/1c2ac00.i2c/i2c-0/0-0038/input ] ; do 	
  rmmod edt_ft5x06
  modprobe edt_ft5x06
  sleep 2
done


