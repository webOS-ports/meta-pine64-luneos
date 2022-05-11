setenv bootmode normal

if test x${volume_key} = xdown ; then
    echo Recovery mode
    setenv bootmode recovery
fi

setenv bootargs rw bootmode=${bootmode} console=tty0 console=ttyS0,115200 no_console_suspend earlycon=uart,mmio32,0x01c28000 panic=10 consoleblank=0 loglevel=1 cma=256M LUNEOS_NO_OUTPUT_REDIRECT

echo Loading DTB...
load mmc ${mmc_bootdev}:1 ${fdt_addr_r} sun50i-a64-pinephone.dtb

echo Loading Initramfs...
load mmc ${mmc_bootdev}:1 ${ramdisk_addr_r} initramfs-uboot-image-pinephone.uboot

echo Loading Kernel...
load mmc ${mmc_bootdev}:1 ${kernel_addr_r} Image

echo Resizing FDT...
fdt addr ${fdt_addr_r}
fdt resize

echo Booting kernel...
booti ${kernel_addr_r} ${ramdisk_addr_r} ${fdt_addr_r}
