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

# Optional overrides, e.g. two units of the same model:
#     LUNEOS_USB_NET=172.16.46
[ -r /etc/default/luneos-usb-gadget ] && . /etc/default/luneos-usb-gadget

# One /24 per machine, so several LuneOS devices can be plugged into one host at
# once. Every device used to take 172.16.42.2 and every host-side link
# 172.16.42.1/24, which is not just a duplicate address: the host then has
# several routes for the same subnet and picks one, so packets for the second
# device leave through the first device's interface. Distinct subnets are what
# actually fixes it.
#
# Keep this table in sync with the copy in meta-mainline's initramfs
# init_functions.sh (setup_usb_network), which has to make the same decision
# before the rootfs exists.
if [ -z "${LUNEOS_USB_NET:-}" ]; then
    case "$(tr -d " \t\n" < /etc/hostname 2>/dev/null)" in
        pinephone)    LUNEOS_USB_NET=172.16.42 ;;
        pinephonepro) LUNEOS_USB_NET=172.16.43 ;;
        pinetab2)     LUNEOS_USB_NET=172.16.44 ;;
        *)            LUNEOS_USB_NET=172.16.45 ;;
    esac
fi
IP="$LUNEOS_USB_NET.2/24"
HOST_IP="$LUNEOS_USB_NET.1"

# Derive the host-side MAC per device instead of using one constant for all of
# them. With an identical MAC the host's predictable-interface-name scheme wants
# to call every link enx<samemac>, so only the first gets that name and the rest
# fall back to usb0/usb1 in probe order - which is why NetworkManager profiles
# here had to be pinned to interface names and kept getting attached to the wrong
# device. First byte stays fa: locally administered (bit 1) and unicast (bit 0).
_id=$(tr -d " \t\n" < /etc/machine-id 2>/dev/null)
[ -n "$_id" ] || _id=$(tr -d "\0" < /proc/device-tree/serial-number 2>/dev/null)
[ -n "$_id" ] || _id=fedcba9876543210
HOST_MAC="fa:$(printf "%s" "$_id" | md5sum | sed "s/\(..\)\(..\)\(..\)\(..\)\(..\).*/\1:\2:\3:\4:\5/")"

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

# Name the gadget after this device rather than "LuneOS Device" with a constant
# serial. Every LuneOS device used to present byte-identical USB descriptors and
# the same 172.16.42.2, so with two plugged in there was nothing on the host to
# tell them apart - udev reported ID_MODEL=LuneOS_Device for both, and only one
# could hold .2, so tooling silently talked to whichever won. Deriving the
# product string from the hostname makes udev report e.g.
# ID_MODEL=LuneOS_pinephone, which is how the halium devices have always looked
# (radon shows up as LuneOS_radon).
gadget_name=$(tr -d " \t\n" < /etc/hostname 2>/dev/null)
[ -n "$gadget_name" ] || gadget_name=$(tr -d "\0" < /proc/device-tree/model 2>/dev/null | tr " /" "--")
[ -n "$gadget_name" ] || gadget_name=device

# A real per-device serial, so ID_SERIAL is unique too. machine-id is the same
# thing luneos-mtp-gadget already uses; the DT serial-number is the fallback for
# an image whose machine-id has not been generated yet.
gadget_serial=$(tr -d " \t\n" < /etc/machine-id 2>/dev/null)
[ -n "$gadget_serial" ] || gadget_serial=$(tr -d "\0" < /proc/device-tree/serial-number 2>/dev/null)
[ -n "$gadget_serial" ] || gadget_serial=fedcba9876543210

mkdir -p "$G/strings/0x409"
echo "$gadget_serial"        > "$G/strings/0x409/serialnumber"
echo "LuneOS"                > "$G/strings/0x409/manufacturer"
echo "LuneOS $gadget_name"   > "$G/strings/0x409/product"
log "gadget identifies as \"LuneOS $gadget_name\" serial $gadget_serial"

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
    log "$N up at $IP (host gets $HOST_IP)"

    # Hand the host its address, so it needs no per-device configuration: one
    # DHCP ("auto") connection profile matches every LuneOS device instead of a
    # static 172.16.42.1/24 profile per device.
    #
    # Deliberately no "option router" and no "option dns": this DHCP server must
    # never become the host's default gateway or resolver. It answers on one
    # point-to-point link with exactly one address and nothing else.
    conf=/run/luneos-usb-gadget/udhcpd.conf
    mkdir -p /run/luneos-usb-gadget
    cat > "$conf" <<EOF
interface $N
start $HOST_IP
end $HOST_IP
max_leases 1
option subnet 255.255.255.0
lease_file /run/luneos-usb-gadget/udhcpd.leases
pidfile /run/luneos-usb-gadget/udhcpd.pid
EOF
    : > /run/luneos-usb-gadget/udhcpd.leases
    if command -v udhcpd >/dev/null 2>&1; then
        # Replace any instance we started earlier (the unit is RemainAfterExit).
        [ -f /run/luneos-usb-gadget/udhcpd.pid ] &&
            kill "$(cat /run/luneos-usb-gadget/udhcpd.pid)" 2>/dev/null || true
        if udhcpd "$conf"; then
            log "serving $HOST_IP to the host over $N"
        else
            log "warning: udhcpd failed; host must configure $HOST_IP/24 itself"
        fi
    else
        log "no udhcpd; host must configure $HOST_IP/24 itself"
    fi
else
    log "warning: $N did not appear"
fi
