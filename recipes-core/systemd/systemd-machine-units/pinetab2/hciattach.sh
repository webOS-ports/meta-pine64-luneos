#!/bin/sh

# first, get the bluetooth address if possible
if [ -e /persist/bluetooth/.bdaddr ] ; then
	bdaddr="$(cat < /persist/bluetooth/.bdaddr)"
else
# this is probably the first run so we don't have a persistent address yet
	bdaddr=""
fi

# hack to force BT to be ON
echo ifname:bt cmd:BT_ON > /dev/bes2600

sleep 5

# call hciattach
/usr/bin/hciattach -s 1500000 /dev/ttyS1 any 1500000 flow nosleep ${bdaddr}

sleep 1

# unblock bluetooth
/usr/sbin/rfkill unblock bluetooth

timeout=10
while [ ! -e /sys/class/bluetooth/hci0 ] ; do
	sleep 1
	if [ "$timeout" -le 0 ]; then
		echo "Could not persist BT mac addr cause the hci0 interface isn't available"
		exit 0
	fi
	timeout=$(($timeout - 1))
done

#check if we have persistent bdaddr already, if not, save it here for next use
if [ ! -e /persist/bluetooth/.bdaddr ] ; then
	mkdir -p /persist/bluetooth
	chmod 755 /persist/bluetooth
	bdaddr=$(/usr/bin/hcitool dev | grep hci | tail -c 18)
	echo -ne $bdaddr > /persist/bluetooth/.bdaddr
fi

