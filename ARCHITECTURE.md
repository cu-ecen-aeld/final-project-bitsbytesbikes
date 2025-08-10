# System Architecture

This document describes the architectural design of the GPS-enabled environmental data station.

## Overview

The system is designed as a dedicated embedded Linux platform for environmental monitoring with GPS positioning. It uses a layered architecture built on Yocto Project to create a minimal, purpose-built Linux distribution.

## System Layers

### Hardware Layer

```
┌─────────────────────────────────────────┐
│              Raspberry Pi               │
├─────────────────┬───────────────────────┤
│   GPIO/I2C      │       UART/Serial     │
│   BME 280       │       GPS Module      │
│   Sensor        │                       │
└─────────────────┴───────────────────────┘
```

**Components:**
- **Raspberry Pi**: Main processing unit (Pi 4 recommended)
- **BME 280 Sensor**: Environmental monitoring (I2C interface)
  - Temperature measurement
  - Humidity measurement
  - Barometric pressure measurement
- **GPS Module**: Location positioning (UART interface)

### Linux Kernel Layer

**Core Kernel Features:**
- Raspberry Pi BSP (Board Support Package)
- I2C subsystem for sensor communication
- UART/Serial drivers for GPS communication
- GPIO control for hardware interfacing

**Custom Kernel Modules:**
- **cu-ecen-aeld-final-drivers**: Custom drivers for project-specific hardware
  - Location: `meta-aesd/recipes-cu-ecen-aeld-final-drivers/`
  - Repository: https://github.com/bitsbytesbikes/cu-ecen-aeld-final-drivers

### System Software Layer

**Core System Services:**
```
┌──────────────────────────────────────────┐
│            Application Layer             │
├──────────────────────────────────────────┤
│        Python Runtime Environment       │
├──────────────────────────────────────────┤
│         System Services & Tools         │
├──────────────────────────────────────────┤
│            Linux Kernel                  │
└──────────────────────────────────────────┘
```

**System Components:**
- **Init System**: SysV init scripts
- **SSH Server**: Remote access and management
- **Python 3**: Runtime for data collection scripts
- **I2C Tools**: Hardware debugging and configuration
- **MTD Utils**: Flash memory management

### Application Layer

**Core Applications:**
- **read.py**: Main data collection application
  - Location: `/aesd/read.py`
  - Handles sensor data reading
  - GPS data processing
  - Data formatting and transmission

**Service Management:**
- **start-aesd-service**: System service initialization
  - Location: `/etc/init.d/start-aesd-service`
  - Automatic startup configuration
  - Service lifecycle management

## Data Flow Architecture

```
┌─────────────┐    I2C     ┌──────────────┐
│  BME 280    ├──────────→ │              │
│  Sensor     │            │   read.py    │
└─────────────┘            │  Application │
                           │              │
┌─────────────┐   UART     │              │
│ GPS Module  ├──────────→ │              │
└─────────────┘            └──────┬───────┘
                                  │
                                  ▼
                           ┌──────────────┐
                           │ Data Output  │
                           │ (Network/Log)│
                           └──────────────┘
```

**Data Collection Flow:**
1. **Sensor Reading**: BME 280 data via I2C interface
2. **GPS Acquisition**: Position data via UART/Serial
3. **Data Processing**: Python application combines and processes data
4. **Data Output**: Processed data transmitted via network or logged locally

## Software Stack

### Yocto Project Layers

```
meta-aesd (Custom Layer)
├── conf/
│   └── layer.conf                 # Layer configuration
├── recipes-aesd/
│   └── images/
│       └── aesd-image.bb         # Custom image recipe
└── recipes-cu-ecen-aeld-final-drivers/
    └── cu-ecen-aeld-final-drivers/
        └── cu-ecen-aeld-final-drivers_git.bb  # Kernel drivers

Standard Layers:
├── meta (poky core)
├── meta-poky
├── meta-yocto-bsp
├── meta-raspberrypi               # Raspberry Pi support
├── meta-openembedded/meta-oe      # Additional packages
└── meta-openembedded/meta-python  # Python support
```

### Package Architecture

**Core Image Recipe** (`aesd-image.bb`):
```bitbake
require recipes-extended/images/core-image-full-cmdline.bb

IMAGE_INSTALL += "libstdc++ mtd-utils"
IMAGE_INSTALL += "openssh openssl openssh-sftp-server"
IMAGE_INSTALL += "python3 python3-pip python3-pyserial python3-requests"
IMAGE_INSTALL += "vim bash"
IMAGE_INSTALL += "kernel-modules i2c-tools"
IMAGE_INSTALL += "cu-ecen-aeld-final-drivers"
```

**Package Dependencies:**
- **Base System**: core-image-full-cmdline
- **Development**: libstdc++, vim, bash
- **Hardware**: i2c-tools, kernel-modules
- **Network**: openssh, openssl
- **Python**: python3 ecosystem with serial and HTTP support
- **Storage**: mtd-utils for flash management
- **Custom**: cu-ecen-aeld-final-drivers

### Runtime Architecture

**Boot Sequence:**
1. **Bootloader**: Raspberry Pi bootloader loads kernel
2. **Kernel Init**: Linux kernel initializes hardware
3. **Init System**: SysV init starts system services
4. **Service Start**: start-aesd-service initializes application
5. **Application**: read.py begins data collection

**Process Architecture:**
```
init (PID 1)
├── SSH daemon
├── start-aesd-service
│   └── read.py (data collection)
└── other system processes
```

## Communication Interfaces

### Hardware Interfaces

**I2C Bus:**
- **Address**: BME 280 sensor (typically 0x76 or 0x77)
- **Speed**: Standard mode (100 kHz) or Fast mode (400 kHz)
- **Data**: Temperature, humidity, pressure readings

**UART/Serial:**
- **Port**: /dev/ttyS0 or /dev/ttyAMA0
- **Baud Rate**: 9600 or 38400 (GPS module dependent)
- **Protocol**: NMEA 0183 GPS protocol

### Network Interfaces

**SSH Access:**
- **Port**: 22 (standard SSH)
- **Authentication**: Key-based or password
- **Purpose**: Remote management and data retrieval

**Data Transmission:**
- **Protocol**: HTTP/HTTPS via Python requests library
- **Format**: JSON or custom data format
- **Destination**: Remote data collection server

## Configuration Management

### Build Configuration

**Local Configuration** (`rpi-build/conf/local.conf`):
- Machine target selection
- Build optimization settings
- Package selection and features

**Layer Configuration** (`rpi-build/conf/bblayers.conf`):
- Layer inclusion and priority
- Build path configuration

### Runtime Configuration

**Boot Configuration:**
- `config.txt`: Hardware initialization parameters
- `cmdline.txt`: Kernel command line parameters

**Service Configuration:**
- Init scripts for automatic service startup
- Application configuration files

## Security Considerations

### System Security

- **Minimal Attack Surface**: Custom minimal Linux image
- **SSH Access Control**: Public key authentication recommended
- **Service Isolation**: Limited running services
- **Update Mechanism**: Controlled system updates via rebuild

### Data Security

- **Local Storage**: Secure data storage on device
- **Network Transmission**: HTTPS encryption for data upload
- **Access Control**: Limited user privileges

## Scalability and Extensibility

### Hardware Extensibility

- **I2C Bus**: Additional sensors can be connected
- **GPIO**: Extra digital I/O for additional hardware
- **USB**: Additional peripherals via USB interface
- **SPI**: Alternative high-speed sensor interface

### Software Extensibility

- **Yocto Recipes**: Easy addition of new packages
- **Python Modules**: Extensible data processing
- **Service Framework**: Additional system services
- **Custom Drivers**: Kernel module framework for new hardware

## Performance Characteristics

### Resource Usage

- **Memory**: Minimal footprint, approximately 512MB RAM usage
- **Storage**: ~2GB image size for complete system
- **CPU**: Low utilization during normal operation
- **Power**: Optimized for battery operation

### Data Throughput

- **Sensor Polling**: Configurable sampling rate (1-60 seconds typical)
- **GPS Update Rate**: 1Hz standard GPS update rate
- **Network Transmission**: Batch data transmission for efficiency

## Future Enhancements

### Planned Features

- **Real-time Data Visualization**: Web interface for live data
- **Data Logging**: Local SQLite database for data storage
- **Wireless Communication**: LoRa or cellular connectivity
- **Power Management**: Sleep modes and power optimization
- **Multiple Sensors**: Support for additional environmental sensors