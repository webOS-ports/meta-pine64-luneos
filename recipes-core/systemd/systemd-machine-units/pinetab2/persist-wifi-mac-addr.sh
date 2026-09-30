#!/bin/sh

# Record the WLAN MAC address for reference.
#
# This is only a record: nothing reads the file back and applies it, and
# nothing needs to.  The address is stable by construction -- the driver takes
# it from local-mac-address, or failing that derives it from the SoC serial
# number -- so it is already the same on every boot.
#
# It used to bail out as soon as the file existed, which meant a value written
# once was kept forever even after the address it named had changed.  That is
# how the de:ad:be:ef:12:34 placeholder from the old device tree outlived it.
# Rewrite the file whenever it disagrees with reality instead.

MACADDR_FILE=/persist/wifi/.macaddr

timeout=10
while [ ! -e /sys/class/net/wlan0 ] ; do
	sleep 1
	if [ "$timeout" -le 0 ]; then
		echo "Could not persist WiFi mac addr cause the network interface isn't available"
		exit 0
	fi
	timeout=$(($timeout - 1))
done

wifi_mac="$(cat < /sys/class/net/wlan0/address)"

if [ -s "$MACADDR_FILE" ] && [ "$(cat < "$MACADDR_FILE")" = "$wifi_mac" ] ; then
	exit 0
fi

mkdir -p /persist/wifi
chmod 755 /persist/wifi
printf '%s' "$wifi_mac" > "$MACADDR_FILE"
echo "Recorded WiFi MAC $wifi_mac"
