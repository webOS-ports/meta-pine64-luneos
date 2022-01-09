LuneOS Pine64 layer
=============
This layer will allow you to build a (partially) functional image of LuneOS for the Pinephone devkit.


## Status

### Pinephone: what works

- Almost everything except camera

### Pinephone: known issues

- Camera doesn't work yet
- Some conflicts seem to happen between wifi and cellular connections at first boot. If the connection to wifi doesn't work on first boot, reboot and use the Wifi app to connect.
- Cellular data has not yet been tested

Note: rendering is done using Mesa and the Mali driver.

### Pinephone Pro: what works

- Screen
- Wifi
- Brightness slider
- Volume buttons, power button
- Modem (powers on, not tested anything else yet)
- Bluetooth (seems to power on, haven't tested it further)
- Brightness sensor
- Audio playback
- Rotation (mostly) and most other sensors
- Power Button

### Pinephone Pro: doesn't work/known issues

- Rotation sensor when screen is turned 180 degrees
- Magnetometer
- Barometer (supposedly included according to press release)?
- Headphones: Sound plays on both headphones and speaker at the same time.

## Instructions

First, setup the webOS ports build directory (see webOS-Ports documentation).

Then, build the image
```
cd webos-ports
. setup-env
MACHINE=pinephone bb luneos-dev-image
```

To flash the image to your SD card, first you need to find which device is the correct one. Run `lsblk` or `df -h` and find the one that looks like your card. For example, mine is /dev/sdd, but it will likely differ for you.

Unmount your drive

```
sudo umount /dev/sdd*
```

Flash the image using wic: 

```
sudo su
. setup-env
sudo wic write tmp-glibc/deploy/images/pinephone/luneos-dev-image-pinephone.wic /dev/sdd
```

**Note**: for Pinephone Pro, replace pinephone with pinephonepro in all the previous steps.

Please note that it's still a work-in-progress code, and at this stage it's useless to report the tons of
issues you'll be experiencing with this image.

## Copyright and License Information

Unless otherwise specified, all content, including all source code files and
documentation files in this repository are:

Copyright (c) 2019 Christophe Chapuis

Unless otherwise specified or set forth in the NOTICE file, all content,
including all source code files and documentation files in this repository are:
Licensed under the Apache License, Version 2.0 (the "License");
you may not use this content except in compliance with the License.
You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.

