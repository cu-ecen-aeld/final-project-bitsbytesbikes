# Hardware Setup and Deployment

This document provides instructions for setting up the hardware components and deploying the GPS-enabled environmental data station.

## Hardware Requirements

### Core Components

**Raspberry Pi**
- **Recommended**: Raspberry Pi 4 Model B (2GB+ RAM)
- **Alternatives**: Raspberry Pi 3 Model B+ (minimum)
- **Storage**: 16GB+ microSD card (Class 10 or better)
- **Power**: 5V/3A USB-C power supply for Pi 4

**Environmental Sensor**
- **BME 280**: Temperature, humidity, and barometric pressure sensor
- **Interface**: I2C communication
- **Voltage**: 3.3V operation
- **Breakout Board**: Recommended for easier prototyping

**GPS Module**
- **Interface**: UART/Serial communication
- **Voltage**: 3.3V operation  
- **Antenna**: External antenna recommended for better reception
- **Protocol**: NMEA 0183 compatible

### Additional Components

**Connectivity**
- microSD card (16GB+ recommended)
- HDMI cable (for initial setup)
- USB keyboard and mouse (for initial setup)
- Ethernet cable or WiFi (for network connectivity)

**Development/Debugging**
- Breadboard or prototyping board
- Jumper wires (male-to-female, male-to-male)
- Multimeter (for voltage verification)
- Logic analyzer (optional, for debugging communication)

## Hardware Connections

### BME 280 Sensor (I2C)

**Pin Connections:**
```
BME 280    →    Raspberry Pi
VCC        →    3.3V (Pin 1)
GND        →    Ground (Pin 6)
SDA        →    GPIO 2 (Pin 3) - I2C Data
SCL        →    GPIO 3 (Pin 5) - I2C Clock
```

**I2C Address:**
- Default: `0x76` or `0x77` (depending on SDO pin connection)
- Use `i2cdetect -y 1` to verify sensor detection

### GPS Module (UART)

**Pin Connections:**
```
GPS Module →    Raspberry Pi
VCC        →    3.3V (Pin 17)
GND        →    Ground (Pin 20)
TX         →    GPIO 15 (Pin 10) - UART RX
RX         →    GPIO 14 (Pin 8)  - UART TX
```

**Serial Configuration:**
- **Device**: `/dev/serial0` or `/dev/ttyAMA0`
- **Baud Rate**: 9600 (typical) or 38400
- **Data Bits**: 8
- **Parity**: None
- **Stop Bits**: 1

### GPIO Pinout Reference

```
Raspberry Pi GPIO Pinout (relevant pins):

     3.3V  (1)  (2)  5V
GPIO2/SDA  (3)  (4)  5V
GPIO3/SCL  (5)  (6)  GND
GPIO4      (7)  (8)  GPIO14/TXD
    GND    (9) (10)  GPIO15/RXD
           ...
     3.3V (17) (18)  GPIO24
           (19)(20)  GND
```

## System Configuration

### Raspberry Pi Boot Configuration

**Enable UART** (`/boot/config.txt`):
```ini
# Enable UART for GPS communication
enable_uart=1

# Disable Bluetooth to free up UART (optional)
dtoverlay=disable-bt

# Enable I2C for sensor communication
dtparam=i2c_arm=on
```

**Boot Command Line** (`/boot/cmdline.txt`):
```
console=serial0,115200 console=tty1 root=PARTUUID=<uuid> rootfstype=ext4 elevator=deadline fsck.repair=yes rootwait
```

### I2C Configuration

**Enable I2C Interface:**
```bash
# Add to /etc/modules
i2c-dev
i2c-bcm2708
```

**Verify I2C Operation:**
```bash
# Install I2C tools (included in image)
sudo apt install i2c-tools

# Scan I2C bus
sudo i2cdetect -y 1

# Expected output should show BME 280 at address 0x76 or 0x77
```

### UART Configuration

**Disable Console on Serial:**
```bash
# Remove console=serial0,115200 from /boot/cmdline.txt if needed for GPS only
# Keep it if you want both GPS and console access
```

**Verify UART Operation:**
```bash
# Check serial devices
ls -l /dev/serial*

# Test GPS data (should show NMEA sentences)
sudo cat /dev/serial0
```

## Deployment Process

### 1. Image Preparation

**Flash Custom Image:**
```bash
# Extract built image
bunzip2 aesd-image-raspberrypi4-64.wic.bz2

# Flash to SD card
sudo dd if=aesd-image-raspberrypi4-64.wic of=/dev/sdX bs=4M status=progress
```

**SD Card Partitions:**
- **Boot Partition**: FAT32 with kernel and config files
- **Root Partition**: ext4 with complete Linux filesystem

### 2. Initial Boot Setup

**First Boot Process:**
1. Insert flashed SD card into Raspberry Pi
2. Connect hardware components as per wiring diagram
3. Connect HDMI display and USB keyboard (for initial setup)
4. Power on the Raspberry Pi

**Initial Configuration:**
```bash
# SSH into the device (if network is configured)
ssh root@<raspberry-pi-ip>

# Or use local console
# Default login: root (password may be required)
```

### 3. Hardware Verification

**Test I2C Communication:**
```bash
# Scan for I2C devices
i2cdetect -y 1

# Should show BME 280 sensor at address 0x76 or 0x77
#      0  1  2  3  4  5  6  7  8  9  a  b  c  d  e  f
# 00:          -- -- -- -- -- -- -- -- -- -- -- -- --
# 10: -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- --
# 20: -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- --
# 30: -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- --
# 40: -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- --
# 50: -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- --
# 60: -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- --
# 70: -- -- -- -- -- -- 76 --
```

**Test GPS Communication:**
```bash
# Check GPS data stream
cat /dev/serial0

# Should show NMEA sentences like:
# $GPGGA,123519,4807.038,N,01131.000,E,1,08,0.9,545.4,M,46.9,M,,*47
# $GPRMC,123519,A,4807.038,N,01131.000,E,022.4,084.4,230394,003.1,W*6A
```

**Test Custom Service:**
```bash
# Check if aesd service is running
ps aux | grep aesd

# Check service status
service start-aesd-service status

# View application logs
tail -f /var/log/messages | grep aesd
```

## Hardware Debugging

### I2C Troubleshooting

**Common Issues:**
- **No device detected**: Check wiring, power supply, and pullup resistors
- **Wrong address**: Verify sensor I2C address configuration
- **Communication errors**: Check for loose connections or voltage issues

**Debug Commands:**
```bash
# Read sensor registers directly
i2cget -y 1 0x76 0xD0  # Should return chip ID (0x60 for BME 280)

# Dump all registers
i2cdump -y 1 0x76
```

### UART Troubleshooting

**Common Issues:**
- **No GPS data**: Check antenna connection and GPS fix
- **Garbled data**: Verify baud rate configuration
- **Permission errors**: Ensure user has access to serial device

**Debug Commands:**
```bash
# Check serial port permissions
ls -l /dev/serial0

# Test with different baud rates
stty -F /dev/serial0 38400
cat /dev/serial0

# Monitor GPS acquisition
minicom -D /dev/serial0 -b 9600
```

## Power Management

### Power Requirements

**Current Consumption:**
- Raspberry Pi 4: 600mA - 1.25A (idle to full load)
- BME 280: 3.4μA (sleep) to 714μA (active)
- GPS Module: 20-40mA (depending on model)
- **Total System**: ~800mA typical operation

### Battery Operation

**For Battery-Powered Deployment:**
- Use high-capacity power bank (20,000mAh+ for extended operation)
- Consider UPS HAT for uninterrupted operation
- Implement sleep modes in software for power savings

**Power Optimization:**
```bash
# Disable unnecessary services
systemctl disable bluetooth
systemctl disable wifi

# Configure CPU governor for power saving
echo "powersave" > /sys/devices/system/cpu/cpu0/cpufreq/scaling_governor
```

## Environmental Considerations

### Operating Conditions

**Temperature Range:**
- **System**: -20°C to +60°C (with proper enclosure)
- **BME 280**: -40°C to +85°C (sensor rating)
- **Raspberry Pi**: 0°C to +60°C (recommended)

**Humidity and Weather Protection:**
- Use weatherproof enclosure for outdoor deployment
- Ensure adequate ventilation for temperature measurement
- Protect connections from moisture and corrosion

### Enclosure Recommendations

**Indoor Deployment:**
- Plastic project box with ventilation holes
- Easy access for SD card and connections

**Outdoor Deployment:**
- Weatherproof IP65+ rated enclosure
- UV-resistant materials
- Cable glands for external connections
- Desiccant packets for moisture control

## Maintenance and Monitoring

### Regular Maintenance

**Weekly Checks:**
- Verify GPS fix quality and data output
- Check sensor readings for accuracy
- Monitor system logs for errors

**Monthly Maintenance:**
- Clean enclosure and check connections
- Verify backup power systems
- Update system logs and data archives

### Remote Monitoring

**SSH Access:**
```bash
# Set up SSH keys for secure access
ssh-keygen -t rsa -b 4096
ssh-copy-id root@<raspberry-pi-ip>

# Create monitoring scripts
#!/bin/bash
# Check system status
uptime
df -h
free -m
cat /sys/class/thermal/thermal_zone0/temp
```

**Health Monitoring:**
- Temperature monitoring via `/sys/class/thermal/thermal_zone0/temp`
- Storage space monitoring via `df -h`
- Memory usage via `free -m`
- Service status checks

## Safety Considerations

### Electrical Safety

- **Voltage Levels**: Verify 3.3V compatibility for all sensors
- **Ground Connections**: Ensure proper grounding for all components
- **Power Supply**: Use appropriate current capacity power supplies
- **ESD Protection**: Use anti-static precautions during assembly

### Data Safety

- **SD Card Backup**: Regular backup of configuration and data
- **Data Redundancy**: Implement local and remote data storage
- **Configuration Management**: Version control for system configurations

## Troubleshooting Common Issues

### Hardware Issues

**Problem**: Raspberry Pi won't boot
- **Check**: Power supply adequate (5V/3A for Pi 4)
- **Check**: SD card properly flashed and inserted
- **Check**: Boot partition files present

**Problem**: Sensor not detected
- **Check**: Wiring connections and voltage levels
- **Check**: I2C enabled in boot configuration
- **Check**: Device address with i2cdetect

**Problem**: GPS no fix
- **Check**: Antenna connection and placement
- **Check**: Clear view of sky for satellite reception
- **Check**: UART configuration and baud rate

### Software Issues

**Problem**: Service not starting
- **Check**: Service script permissions and configuration
- **Check**: Application dependencies installed
- **Check**: System logs for error messages

**Problem**: Data collection errors
- **Check**: Sensor initialization and calibration
- **Check**: Communication interfaces operational
- **Check**: Application logs for specific errors