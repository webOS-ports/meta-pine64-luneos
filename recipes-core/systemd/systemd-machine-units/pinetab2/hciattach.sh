#!/bin/sh

# The BES2600 has no BD_ADDR of its own, so one has to be supplied on the
# hciattach command line or the controller comes up as 00:00:00:00:00:00.
#
# This used to be handled by attaching without an address and then saving
# whatever "hcitool dev" reported, which cannot work: with no address passed
# in there is nothing for hcitool to report, so the saved file stayed empty
# and every boot started from the same nothing.
#
# Instead derive the address from the SoC serial number, the same per-device
# value the WLAN driver hashes for its own MAC.  That gives an address which
# is stable across reboots and reflashes and differs between tablets, with no
# first-boot chicken-and-egg.  A previously persisted address still wins, so
# devices that already have one keep it.

BDADDR_FILE=/persist/bluetooth/.bdaddr

is_valid_bdaddr() {
	case "$1" in
	[0-9a-fA-F][0-9a-fA-F]:[0-9a-fA-F][0-9a-fA-F]:[0-9a-fA-F][0-9a-fA-F]:[0-9a-fA-F][0-9a-fA-F]:[0-9a-fA-F][0-9a-fA-F]:[0-9a-fA-F][0-9a-fA-F])
		[ "$1" != "00:00:00:00:00:00" ] ;;
	*)
		false ;;
	esac
}

derive_bdaddr() {
	serial="$(tr -d '\0' < /proc/device-tree/serial-number 2>/dev/null)"
	[ -n "$serial" ] || return 1

	# "bt-" keeps this distinct from the WLAN address derived from the same
	# serial, so the two radios never land on the same value.
	# Uppercase throughout, so the reserved-LAP test below can match and the
	# stored address does not change spelling depending on where it came from.
	hex="$(printf 'bt-%s' "$serial" | md5sum | cut -c1-12 | tr 'a-f' 'A-F')"
	[ ${#hex} -eq 12 ] || return 1

	o1=$(printf '%d' "0x$(echo "$hex" | cut -c1-2)")
	# Mark it locally administered: no OUI has been assigned for this, and
	# clear the multicast bit, which is not valid in an address.
	o1=$(( (o1 | 2) & 254 ))

	bdaddr="$(printf '%02X:%s:%s:%s:%s:%s' "$o1" \
		"$(echo "$hex" | cut -c3-4)"  "$(echo "$hex" | cut -c5-6)" \
		"$(echo "$hex" | cut -c7-8)"  "$(echo "$hex" | cut -c9-10)" \
		"$(echo "$hex" | cut -c11-12)")"

	# The 0x9E8B00-0x9E8B3F LAP range is reserved for inquiry, so an address
	# ending up in it would not be usable.  Nudging one bit is enough.
	case "$bdaddr" in
	*:9E:8B:0[0-9A-F]|*:9E:8B:1[0-9A-F]|*:9E:8B:2[0-9A-F]|*:9E:8B:3[0-9A-F])
		bdaddr="${bdaddr%:*:*:*}:9E:8C:${bdaddr##*:}" ;;
	esac

	return 0
}

bdaddr=""
if [ -s "$BDADDR_FILE" ] ; then
	bdaddr="$(cat < "$BDADDR_FILE")"
	is_valid_bdaddr "$bdaddr" || bdaddr=""
fi

if [ -z "$bdaddr" ] && derive_bdaddr ; then
	echo "Derived BT address $bdaddr from the SoC serial"
	mkdir -p /persist/bluetooth
	chmod 755 /persist/bluetooth
	printf '%s' "$bdaddr" > "$BDADDR_FILE"
fi

if [ -z "$bdaddr" ] ; then
	echo "No persisted and no derivable BT address, letting the controller pick one"
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

# Nothing was persisted above only if the address could not be derived; in that
# case take whatever the controller ended up with, as before.
if [ ! -s "$BDADDR_FILE" ] ; then
	mkdir -p /persist/bluetooth
	chmod 755 /persist/bluetooth
	bdaddr=$(/usr/bin/hcitool dev | grep hci | tail -c 18)
	if is_valid_bdaddr "$bdaddr" ; then
		printf '%s' "$bdaddr" > "$BDADDR_FILE"
	fi
fi
