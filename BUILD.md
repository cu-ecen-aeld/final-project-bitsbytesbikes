# Build Instructions

This document provides step-by-step instructions for building the GPS-enabled environmental data station Linux image.

## Prerequisites

### System Requirements

- Linux development machine (Ubuntu 18.04+ recommended)
- At least 50GB of free disk space
- 8GB+ RAM recommended
- Internet connection for downloading dependencies

### Required Packages

Install Yocto Project dependencies:

```bash
# Ubuntu/Debian
sudo apt update
sudo apt install gawk wget git diffstat unzip texinfo gcc build-essential \
    chrpath socat cpio python3 python3-pip python3-pexpect xz-utils \
    debianutils iputils-ping python3-git python3-jinja2 libegl1-mesa \
    libsdl1.2-dev pylint3 xterm python3-subunit mesa-common-dev zstd liblz4-tool

# CentOS/RHEL/Fedora
sudo yum groupinstall "Development Tools"
sudo yum install gawk make wget tar bzip2 gzip python3 unzip perl patch \
    diffutils diffstat git cpp gcc gcc-c++ glibc-devel texinfo chrpath \
    ccache perl-Data-Dumper perl-Text-ParseWords perl-Thread-Queue \
    python3-pip xz which SDL-devel xterm zstd lz4
```

## Getting the Source

1. **Clone the repository with submodules:**
   ```bash
   git clone --recursive https://github.com/cu-ecen-aeld/final-project-bitsbytesbikes.git
   cd final-project-bitsbytesbikes
   ```

2. **Initialize submodules (if not using --recursive):**
   ```bash
   git submodule update --init --recursive
   ```

## Build Environment Setup

1. **Set up the build environment:**
   ```bash
   source poky/oe-init-build-env rpi-build
   ```

   This command:
   - Sources the Yocto build environment
   - Changes to the `rpi-build` directory
   - Sets up BitBake environment variables

2. **Verify configuration files:**
   - `conf/local.conf` - Build configuration
   - `conf/bblayers.conf` - Layer configuration

## Configuration

### Machine Configuration

The build is pre-configured for Raspberry Pi. Key settings in `conf/local.conf`:

- **Machine Target**: `MACHINE = "raspberrypi4-64"` (or appropriate Pi model)
- **Host Name**: Custom hostname configuration
- **Serial Console**: UART enabled for debugging

### Boot Configuration

Ensure these settings for proper boot:

**config.txt** (in boot partition):
```
enable_uart=1
```

**cmdline.txt** (in boot partition):
```
console=serial0,115200 console=tty1
```

## Building the Image

1. **Build the custom image:**
   ```bash
   bitbake aesd-image
   ```

   This process will:
   - Download all required source packages
   - Cross-compile the kernel and userspace
   - Create the root filesystem
   - Generate the final image

   **Note**: First build can take 2-6 hours depending on your system.

2. **Build output location:**
   ```
   tmp/deploy/images/raspberrypi4-64/aesd-image-raspberrypi4-64.wic.bz2
   ```

## Image Components

The built image includes:

### Core System
- Linux kernel with Raspberry Pi support
- Minimal root filesystem based on core-image-full-cmdline
- Standard development tools (vim, bash)

### Networking & Remote Access
- OpenSSH server and client
- OpenSSL libraries
- SFTP server support

### Hardware Support
- I2C tools for sensor communication
- UART/serial communication support
- MTD utilities for flash memory management

### Python Environment
- Python 3 runtime
- pip package manager
- pyserial for serial communication
- requests library for HTTP communication

### Custom Components
- **cu-ecen-aeld-final-drivers**: Custom kernel drivers for hardware interfacing
- **read.py**: Environmental data collection script
- **start-aesd-service**: System service initialization script

## Deploying the Image

1. **Extract the image:**
   ```bash
   bunzip2 tmp/deploy/images/raspberrypi4-64/aesd-image-raspberrypi4-64.wic.bz2
   ```

2. **Flash to SD card:**
   ```bash
   sudo dd if=tmp/deploy/images/raspberrypi4-64/aesd-image-raspberrypi4-64.wic \
           of=/dev/sdX bs=4M status=progress conv=fsync
   ```
   
   **⚠️ Warning**: Replace `/dev/sdX` with your actual SD card device. Double-check with `lsblk`.

3. **Safely eject:**
   ```bash
   sudo eject /dev/sdX
   ```

## Development Workflow

### Incremental Builds

For faster rebuilds during development:

```bash
# Clean specific package
bitbake -c clean cu-ecen-aeld-final-drivers

# Rebuild specific package
bitbake cu-ecen-aeld-final-drivers

# Rebuild complete image
bitbake aesd-image
```

### Debugging Builds

View build logs:
```bash
# Show recent log
bitbake -c devshell aesd-image

# View specific logs
less tmp/log/cooker/raspberrypi4-64/console-latest.log
```

## Troubleshooting

### Common Issues

1. **Disk Space**: Ensure sufficient space (50GB+) before building
2. **Network Issues**: Check firewall settings if downloads fail
3. **Permissions**: Avoid building as root user
4. **Submodules**: Ensure all submodules are properly initialized

### Build Errors

- Check `tmp/log/cooker/` for detailed error logs
- Use `bitbake -c clean <recipe>` to clean problematic packages
- Verify all dependencies are installed

### Getting Help

- Check build logs in `tmp/log/`
- Review Yocto Project documentation
- Consult project-specific notes in `Notes.txt`

## Performance Optimization

- **Parallel Builds**: Set `BB_NUMBER_THREADS` and `PARALLEL_MAKE` in local.conf
- **Shared State Cache**: Use `SSTATE_DIR` for faster rebuilds
- **Download Cache**: Set `DL_DIR` to cache downloads across builds