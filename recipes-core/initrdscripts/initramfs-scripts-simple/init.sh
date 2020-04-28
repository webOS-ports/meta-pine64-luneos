#! /bin/sh

. /init_functions.sh

export PATH=$PATH:/sbin:/bin:/usr/sbin:/usr/bin

echo "Mounting pseudo-filesystems"
mkdir -m 0755 /proc
mkdir -m 0755 /sys
mkdir -p /dev

# mount basic virtual fs
mount_proc_sys_dev_configfs ""
# populate /dev thanks to mdev
start_mdev
# redirect log
setup_log

setup_usb_network 172.16.42.2/16

if grep -q bootmode=recovery /proc/cmdline; then
    # Add root user
    cat > /etc/passwd << "EOF"
root::0:0:root:/root:/bin/sh
EOF
    
    # start telnetd for this IP
    start_telnetd 172.16.42.2
    
    # start... a shell ? a nice UI ?
    /usr/bin/luneos_recovery_ui || sh
   
else
    # mount partition labeled "luneos-rootfs"
    mount_root_partition "luneos-rootfs" "/rfs"

    mount_proc_sys_dev_configfs "/rfs"

    #info "Stopping debug services"
    #stop_telnetd
    stop_mdev

    info "Umounting unneeded filesystems"
    umount_proc_sys_dev_configfs ""

    info "Switching to root filesystem"
    exec switch_root /rfs /sbin/init
fi
