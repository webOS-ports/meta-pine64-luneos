#!/bin/sh
# This file will be in /init_functions.sh inside the initramfs.

# Redirect stdout and stderr to logfile
setup_log() {
	# default redirect goes to /dev/kmsg
	[ -e /dev/kmsg ] && exec >/dev/kmsg 2>&1
	# Bail out if PMOS_NO_OUTPUT_REDIRECT is set
	echo "======= LuneOS initrd ==========="
	grep -q LUNEOS_NO_OUTPUT_REDIRECT /proc/cmdline && return

	# Print a message about what is going on to the normal output
	echo "NOTE: All output from the initramfs gets redirected to:"
	echo "/LuneOS_init.log"
	echo "If you want to disable this behavior (e.g. because you're"
	echo "debugging over serial), please add this to your kernel"
	echo "command line: LUNEOS_NO_OUTPUT_REDIRECT"

	# Start redirect, print the first line again
	exec >/LuneOS_init.log 2>&1
	echo "======= LuneOS initrd ==========="
}

info() {
    echo "$1"
}

fail() {
    echo "$distro_name initramfs failed:"
    echo "$1"
    echo "Waiting for 5 seconds before rebooting"
    sleep 5
    mkdir -p /boottmp
    mount /dev/mmcblk0p1 /boottmp
    [ -e /LuneOS_init.log ] && cp /LuneOS_init.log /boottmp/LuneOS_init.log
    dmesg >> /boottmp/LuneOS_init.log
    umount /boottmp
    loop_forever
}

# $1: root directory for mounts
mount_proc_sys_dev_configfs() {
	# mdev
	mount -t proc -o nodev,noexec,nosuid proc $1/proc
	mount -t sysfs -o nodev,noexec,nosuid sysfs $1/sys
	
	mkdir $1/config
	mount -t configfs -o nodev,noexec,nosuid configfs $1/config
}

# $1: root directory for mounts
umount_proc_sys_dev_configfs() {
	umount -l $1/config
	umount -l $1/sys
	umount -l $1/proc
}

start_mdev() {
	echo /sbin/mdev > /proc/sys/kernel/hotplug
	mdev -s
}

stop_mdev() {
	killall mdev
	echo "" > /proc/sys/kernel/hotplug
}

start_telnetd() {
	# /dev/pts (needed for telnet)
	mkdir /dev/pts
	mount -t devpts none /dev/pts
	
	echo "Starting telnetd..."
	/usr/sbin/telnetd
	echo "Pidof telnetd: $(pidof telnetd)"
}

stop_telnetd() {
	killall telnetd
	umount /dev/pts
}

# $1: label of the partition
# $2: target directory of mount
mount_root_partition() {
	partname=$1
	rfs=$2

	if [ -z "$partname" ] ; then
		partname="luneos-rootfs"
	fi

    part=$(find /dev -name $partname | tail -1)
    if [ -n "$part" ]; then
		rootfs_path=$(readlink -f $part)
    fi

	mkdir -p $rfs
	mount -t ext4 -o rw,noatime,nodiratime $rootfs_path $rfs
	[ $? -eq 0 ] || fail "Failed to mount rootfs partition $rootfs_path,$partname,$part on $rfs"
}

setup_usb_network_configfs() {
	# Only run, when we have the gadget usb driver
	CONFIGFS=/config/usb_gadget
	[ -e "$CONFIGFS" ] || return

	# Create new gadget module template
	mkdir $CONFIGFS/g1
	# Congifure vendor and product IDs
	printf "%s" "0x18D1" >"$CONFIGFS/g1/idVendor"
	printf "%s" "0xD001" >"$CONFIGFS/g1/idProduct"

	# Setup english strings
	mkdir $CONFIGFS/g1/strings/0x409
	echo "0123456789" > $CONFIGFS/g1/strings/0x409/serialnumber
	echo "LuneOS" > $CONFIGFS/g1/strings/0x409/manufacturer
	echo "LuneOS device" > $CONFIGFS/g1/strings/0x409/product

	# Create function instances
	#mkdir $CONFIGFS/g1/functions/ffs.adb
	#mkdir $CONFIGFS/g1/functions/ffs.mtp
	mkdir $CONFIGFS/g1/functions/ecm.usb0

	# Create configuration instance
	mkdir $CONFIGFS/g1/configs/c.1
	mkdir $CONFIGFS/g1/configs/c.1/strings/0x409
	echo "120" > $CONFIGFS/g1/configs/c.1/MaxPower
	printf "%s" "rndis" > $CONFIGFS/g1/configs/c.1/strings/0x409/configuration

	# Bind function instances and their configuration
	# NOTE: binding ffs currently doesn't work and will disable ECM...
	#ln -s $CONFIGFS/g1/functions/ffs.adb $CONFIGFS/g1/configs/c.1
	#ln -s $CONFIGFS/g1/functions/ffs.mtp $CONFIGFS/g1/configs/c.1
	ln -s $CONFIGFS/g1/functions/ecm.usb0 $CONFIGFS/g1/configs/c.1

	echo "$(ls /sys/class/udc)" > $CONFIGFS/g1/UDC
}

# $1: IP address of usb interface
setup_usb_network() {
	# Run all usb network setup functions (add more below!)
	setup_usb_network_configfs

	# Setup usb IP address
	IP=$1
	for INTERFACE in usb0 rndis0 eth0; do
		# try to setup interface. If it fails, try the next one.
		ip address add "$IP" dev $INTERFACE || continue
		# It succeeded, now bring it up and exit
		ip link set $INTERFACE up
		break
	done
}

# $1: path to ppm.gz file
show_splash() {
	# Skip for non-framebuffer devices
	# shellcheck disable=SC2154
	if [ "$deviceinfo_no_framebuffer" = "true" ]; then
		echo "NOTE: Skipping framebuffer splashscreen (deviceinfo_no_framebuffer)"
		return
	fi

	gzip -c -d "$1" >/tmp/splash.ppm
	fbsplash -s /tmp/splash.ppm
}

loop_forever() {
	while true; do
		sleep 1
	done
}
