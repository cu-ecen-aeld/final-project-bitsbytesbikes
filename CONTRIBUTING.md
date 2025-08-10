# Contributing Guidelines

Thank you for your interest in contributing to the GPS-enabled environmental data station project! This document provides guidelines for developers who want to contribute to the project.

## Development Environment Setup

### Prerequisites

1. **Linux Development Machine**: Ubuntu 18.04+ or equivalent
2. **Development Tools**: Git, text editor/IDE of choice
3. **Yocto Dependencies**: See [BUILD.md](BUILD.md) for complete list
4. **Hardware Access**: Raspberry Pi and sensors for testing (optional for software-only contributions)

### Getting Started

1. **Fork the Repository**:
   ```bash
   # Fork on GitHub, then clone your fork
   git clone https://github.com/YOUR_USERNAME/final-project-bitsbytesbikes.git
   cd final-project-bitsbytesbikes
   ```

2. **Set Up Development Environment**:
   ```bash
   # Initialize submodules
   git submodule update --init --recursive
   
   # Set up build environment
   source poky/oe-init-build-env rpi-build
   ```

3. **Create Development Branch**:
   ```bash
   git checkout -b feature/your-feature-name
   ```

## Project Structure

### Repository Organization

```
final-project-bitsbytesbikes/
├── README.md                 # Project overview
├── BUILD.md                  # Build instructions
├── ARCHITECTURE.md           # System design documentation
├── HARDWARE.md              # Hardware setup guide
├── CONTRIBUTING.md          # This file
├── Notes.txt                # Development notes
├── .gitmodules              # Git submodule configuration
├── poky/                    # Yocto Project core (submodule)
├── meta-raspberrypi/        # Raspberry Pi BSP layer (submodule)
├── meta-openembedded/       # Additional packages layer (submodule)
├── meta-aesd/               # Custom project layer
│   ├── conf/
│   │   └── layer.conf       # Layer configuration
│   ├── recipes-aesd/
│   │   └── images/
│   │       └── aesd-image.bb # Custom image recipe
│   └── recipes-cu-ecen-aeld-final-drivers/
│       └── cu-ecen-aeld-final-drivers/
│           └── cu-ecen-aeld-final-drivers_git.bb # Kernel drivers recipe
├── rpi-build/               # Build configuration directory
│   └── conf/
│       ├── local.conf       # Build configuration
│       └── bblayers.conf    # Layer configuration
└── images/                  # Documentation images
```

### Key Files for Development

**Yocto Recipes**:
- `meta-aesd/recipes-aesd/images/aesd-image.bb` - Main image recipe
- `meta-aesd/recipes-cu-ecen-aeld-final-drivers/` - Kernel driver integration
- `rpi-build/conf/local.conf` - Build configuration
- `rpi-build/conf/bblayers.conf` - Layer inclusion

**Documentation**:
- All `.md` files in root directory
- Inline documentation in recipe files

## Contribution Types

### 1. Documentation Improvements

**Areas for Contribution**:
- Clarifying build instructions
- Adding troubleshooting sections
- Improving architecture diagrams
- Hardware setup guides
- API documentation

**Process**:
1. Edit markdown files directly
2. Test instructions on clean system if possible
3. Submit pull request with clear description

### 2. Yocto Recipe Enhancements

**Common Improvements**:
- Adding new packages to the image
- Optimizing build configuration
- Improving package management
- Adding new layers or recipes

**Development Process**:
```bash
# Create new recipe
cd meta-aesd/recipes-aesd/
mkdir new-package
cd new-package
# Create package.bb file

# Test build
bitbake new-package

# Integrate into image
# Edit aesd-image.bb to add new package
```

### 3. Hardware Driver Development

**Driver Development Workflow**:
1. Fork the driver repository: https://github.com/bitsbytesbikes/cu-ecen-aeld-final-drivers
2. Develop and test kernel modules
3. Update recipe to pull latest driver changes
4. Test complete system integration

### 4. Application Development

**Python Application Improvements**:
- Enhancing data collection scripts
- Adding new sensor support
- Improving error handling
- Adding data processing features

**Service Management**:
- Improving startup scripts
- Adding systemd support
- Implementing health monitoring

### 5. Build System Improvements

**Build Optimization**:
- Reducing image size
- Improving build times
- Adding development tools
- Package management improvements

## Development Guidelines

### Code Style

**BitBake Recipes**:
- Follow Yocto Project coding standards
- Use consistent indentation (4 spaces)
- Add comments for complex operations
- Use meaningful variable names

**Python Code**:
- Follow PEP 8 style guidelines
- Use meaningful function and variable names
- Add docstrings for functions and classes
- Include error handling and logging

**Shell Scripts**:
- Use `#!/bin/bash` shebang
- Add error checking (`set -e`)
- Use meaningful variable names
- Add usage documentation

### Documentation Standards

**Markdown Files**:
- Use clear, descriptive headings
- Include code examples where applicable
- Add table of contents for long documents
- Use consistent formatting

**Code Comments**:
- Explain why, not just what
- Keep comments up to date with code changes
- Use clear, concise language

### Testing Guidelines

**Build Testing**:
```bash
# Test recipe changes
bitbake -c clean your-recipe
bitbake your-recipe

# Test complete image build
bitbake aesd-image

# Test on hardware if possible
```

**Hardware Testing**:
- Test on actual Raspberry Pi hardware when possible
- Verify sensor communication
- Test complete data collection pipeline
- Document test results

### Version Control

**Branch Naming**:
- `feature/description` - New features
- `bugfix/description` - Bug fixes
- `docs/description` - Documentation updates
- `refactor/description` - Code refactoring

**Commit Messages**:
```
component: brief description of change

Longer explanation of what changed and why if needed.
Include references to issues or pull requests.

Signed-off-by: Your Name <your.email@example.com>
```

**Pull Request Process**:
1. Create descriptive pull request title
2. Include detailed description of changes
3. Reference related issues
4. Include testing information
5. Request review from maintainers

## Testing Requirements

### Pre-Submission Testing

**Required Tests**:
1. **Build Test**: Verify clean build of modified components
2. **Integration Test**: Ensure changes don't break existing functionality
3. **Documentation Test**: Verify documentation builds and renders correctly

**Recommended Tests**:
1. **Hardware Test**: Test on actual hardware if changes affect hardware
2. **Performance Test**: Verify no performance regressions
3. **Compatibility Test**: Test across different Raspberry Pi models

### Test Reporting

**Include in Pull Request**:
- Build test results
- Hardware test results (if applicable)
- Performance impact assessment
- Documentation verification

## Issue Reporting

### Bug Reports

**Include the Following Information**:
- Clear description of the problem
- Steps to reproduce
- Expected vs. actual behavior
- System information (Pi model, image version)
- Relevant log outputs
- Hardware configuration

**Template**:
```markdown
## Bug Description
Brief description of the issue

## Steps to Reproduce
1. Step one
2. Step two
3. Step three

## Expected Behavior
What should happen

## Actual Behavior
What actually happens

## System Information
- Raspberry Pi Model: 
- Image Version: 
- Hardware Configuration: 

## Logs
Include relevant log outputs
```

### Feature Requests

**Include the Following**:
- Clear description of desired feature
- Use case and motivation
- Proposed implementation approach
- Potential impact on existing functionality

## Review Process

### Code Review Guidelines

**For Reviewers**:
- Check for adherence to coding standards
- Verify functionality and logic
- Test build compatibility
- Review documentation completeness
- Provide constructive feedback

**For Contributors**:
- Respond to review comments promptly
- Make requested changes or provide rationale
- Keep pull requests focused and manageable
- Update documentation as needed

### Approval Process

**Requirements for Merge**:
1. At least one approval from maintainer
2. All automated tests passing
3. Documentation updated as needed
4. No unresolved review comments

## Development Tips

### Efficient Development Workflow

**Incremental Development**:
```bash
# Make small changes and test frequently
bitbake -c clean target-recipe
bitbake target-recipe

# Use shared state cache for faster builds
# Set SSTATE_DIR in local.conf
```

**Debugging Builds**:
```bash
# Get detailed build information
bitbake -v target-recipe

# Debug specific task
bitbake -c devshell target-recipe

# Check dependencies
bitbake -g target-recipe
```

### Common Pitfalls

**Avoid These Issues**:
- Making changes without testing builds
- Hardcoding paths specific to your environment
- Ignoring existing coding standards
- Breaking backward compatibility without discussion
- Adding large binary files to repository

### Resources

**Useful References**:
- [Yocto Project Documentation](https://docs.yoctoproject.org/)
- [BitBake User Manual](https://docs.yoctoproject.org/bitbake/)
- [Raspberry Pi Documentation](https://www.raspberrypi.org/documentation/)
- [Python PEP 8 Style Guide](https://www.python.org/dev/peps/pep-0008/)

## Getting Help

### Communication Channels

**For Questions**:
- Open an issue for general questions
- Use pull request comments for code-specific questions
- Check existing documentation first

**For Discussions**:
- Use GitHub Discussions for design discussions
- Open issues for feature proposals
- Reference relevant documentation

### Mentorship

**New Contributors**:
- Start with documentation improvements
- Look for "good first issue" labels
- Ask questions in issues or pull requests
- Follow the development workflow gradually

## Recognition

### Contributor Acknowledgment

All contributors will be acknowledged in:
- Project documentation
- Release notes
- Contributor list

### Licensing

By contributing to this project, you agree that your contributions will be licensed under the same license as the project. Ensure you have the right to contribute any code or content you submit.

## Contact Information

**Project Maintainers**:
- See GitHub repository contributors and maintainers
- Open issues for technical questions
- Use GitHub discussions for general topics

Thank you for contributing to the GPS-enabled environmental data station project!