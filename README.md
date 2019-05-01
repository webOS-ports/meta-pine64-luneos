LuneOS Pine64 layer
=============
This layer will allow you to build a (partially) functional image of LuneOS for the Pinephone devkit.

## What works?

So far, there are still lots to do, only the essential stuff works:
- touchscreen
- display (using Mali DRM driver)
- apps
 
This means the following hasn't been integrated yet:
- no WiFi
- no Bluetooth
- no calls or SMS

Also note that Mali driver is still very young, and there are lots of graphical artefacts on the screen.

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

Please note that it's a work-in-progress code, and at this stage it's useless to report the tons of
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

