#!/bin/sh
# Create the LuneOS configfs USB gadget (ECM network at 172.16.42.2).
#
# On the PineTab2 this is done by the initramfs (init.sh -> setup_usb_network).
# The PinePhone Pro and PinePhone cannot rely on that: the PPP's initramfs is
# packed as cpio.gz.u-boot, which the kernel refuses to unpack out of a FIT
# ramdisk node ("Initramfs unpacking failed: invalid magic..."), and even when
# it does unpack, init.sh looks for a rootfs labelled "luneos-root*" while the
# PPP's partition is labelled "rootfsA". Doing it from the rootfs avoids both
# problems entirely - by the time we run, the UDC is registered and idle.
#
# Deliberately idempotent: if any gadget is already bound to a UDC (e.g. the
# initramfs got there first), do nothing and leave it alone.
#
# BusyBox-safe: POSIX sh, no arrays, no bashisms.

set -e

GROOT=/sys/kernel/config/usb_gadget
G=$GROOT/g1
N=usb0
C=1
IP=172.16.42.2/16
# First byte must be even (locally administered unicast).
HOST_MAC=FA:75:7F:BB:F4:E6

log() { echo "luneos-usb-gadget: $*"; }

[ -d "$GROOT" ] || { log "configfs usb_gadget not available, nothing to do"; exit 0; }

# Someone else already owns a bound gadget - do not fight over the UDC.
for g in "$GROOT"/*/; do
    [ -d "$g" ] || continue
    if [ -n "$(cat "$g/UDC" 2>/dev/null)" ]; then
        log "gadget $(basename "$g") already bound to $(cat "$g/UDC"), leaving it alone"
        exit 0
    fi
done

# The UDC appears when dwc3 probes, which can be a little after us.
udc=""
i=0
while [ $i -lt 30 ]; do
    udc=$(ls /sys/class/udc 2>/dev/null | head -n 1)
    [ -n "$udc" ] && break
    i=$((i + 1))
    sleep 1
done
[ -n "$udc" ] || { log "no UDC appeared after 30s, giving up"; exit 0; }
log "using UDC $udc"

mkdir -p "$G"
echo 0x1d6b > "$G/idVendor"      # Linux Foundation
echo 0x0104 > "$G/idProduct"     # Multifunction Composite Gadget
echo 0x0100 > "$G/bcdDevice"
echo 0x0200 > "$G/bcdUSB"

mkdir -p "$G/strings/0x409"
echo "fedcba9876543210" > "$G/strings/0x409/serialnumber"
echo "LuneOS"           > "$G/strings/0x409/manufacturer"
echo "LuneOS Device"    > "$G/strings/0x409/product"

mkdir -p "$G/functions/ecm.$N"
echo "$HOST_MAC" > "$G/functions/ecm.$N/host_addr"

mkdir -p "$G/configs/c.$C/strings/0x409"
echo "Config $C: ECM network" > "$G/configs/c.$C/strings/0x409/configuration"
echo 250 > "$G/configs/c.$C/MaxPower"
[ -e "$G/configs/c.$C/ecm.$N" ] || ln -s "$G/functions/ecm.$N" "$G/configs/c.$C/"

# Binding can transiently fail with EBUSY while dwc3 settles, and under
# "set -e" that would abort the script without a word. Retry, and report.
i=0
while [ $i -lt 5 ]; do
    echo "$udc" > "$G/UDC" 2>/dev/null || true
    [ -n "$(cat "$G/UDC" 2>/dev/null)" ] && break
    i=$((i + 1))
    sleep 1
done
if [ -z "$(cat "$G/UDC" 2>/dev/null)" ]; then
    log "failed to bind gadget to $udc after 5 attempts"
    exit 1
fi
log "gadget bound to $(cat "$G/UDC")"

# The netdev only exists once the gadget is bound.
i=0
while [ $i -lt 10 ]; do
    [ -e "/sys/class/net/$N" ] && break
    i=$((i + 1))
    sleep 1
done
if [ -e "/sys/class/net/$N" ]; then
    ip address add "$IP" dev "$N" 2>/dev/null || true
    ip link set "$N" up
    log "$N up at $IP"
else
    log "warning: $N did not appear"
fi
