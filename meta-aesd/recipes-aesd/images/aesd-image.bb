require recipes-extended/images/core-image-full-cmdline.bb

IMAGE_INSTALL += "libstdc++ mtd-utils"
IMAGE_INSTALL += "openssh openssl openssh-sftp-server"
IMAGE_INSTALL += "python3 python3-pip python3-pyserial python3-requests"
IMAGE_INSTALL += "vim bash"
IMAGE_INSTALL += "kernel-modules i2c-tools"
IMAGE_INSTALL += "cu-ecen-aeld-final-drivers"

MACHINE_EXTRA_RRECOMMENDS += "cu-ecen-aeld-final-drivers"

