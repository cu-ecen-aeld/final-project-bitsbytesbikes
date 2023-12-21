require recipes-extended/images/core-image-full-cmdline.bb

IMAGE_INSTALL += "libstdc++ mtd-utils"
IMAGE_INSTALL += "openssh openssl openssh-sftp-server"
IMAGE_INSTALL += "python3 python3-pip python3-pyserial"
IMAGE_INSTALL += "vim bash"
IMAGE_INSTALL += "kernel-modules"

