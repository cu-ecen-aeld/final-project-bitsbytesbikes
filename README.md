# GPS-Enabled Environmental Data Station

A Linux-based environmental monitoring system built with Yocto Project for Raspberry Pi that combines GPS positioning with environmental sensor data collection.

## Project Overview

This project creates a custom Linux distribution for Raspberry Pi that serves as a GPS-enabled environmental data station. The system collects environmental data using sensors (including BME 280 for temperature, humidity, and pressure) and provides GPS positioning capabilities.

### Key Features

- **Custom Linux Image**: Built using Yocto Project/BitBake for minimal, purpose-built system
- **Environmental Monitoring**: BME 280 sensor support for temperature, humidity, and barometric pressure
- **GPS Positioning**: GPS functionality for location-aware data collection
- **Python Environment**: Pre-configured Python 3 with serial communication and HTTP requests
- **Remote Access**: SSH server for remote management and data retrieval
- **Kernel Drivers**: Custom kernel modules for hardware interfacing

### Hardware Target

- **Primary Platform**: Raspberry Pi
- **Sensors**: BME 280 environmental sensor
- **GPS Module**: GPS receiver (specific model TBD)
- **Communication**: UART/Serial, I2C

## Quick Start

1. **Prerequisites**: Linux development machine with Yocto Project dependencies
2. **Clone**: `git clone --recursive <repository-url>`
3. **Build**: See [BUILD.md](BUILD.md) for detailed instructions
4. **Deploy**: Flash image to SD card and boot on Raspberry Pi

## Documentation

- 📖 [Detailed Build Instructions](BUILD.md)
- 🏗️ [System Architecture](ARCHITECTURE.md) 
- 🔧 [Hardware Setup](HARDWARE.md)
- 🤝 [Contributing Guidelines](CONTRIBUTING.md)

## External Resources

- [Project Overview Wiki](https://github.com/cu-ecen-aeld/final-project-bitsbytesbikes/wiki/Project-Overview-%E2%80%90-GPS%E2%80%90enabled-environmental-data-station)
- [Project Schedule](https://github.com/users/bitsbytesbikes/projects/1/views/1?groupedBy%5BcolumnId%5D=69037586)

## License

This project is part of the CU ECEN Advanced Embedded Linux Development course.

