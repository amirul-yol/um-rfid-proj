# RFID Inventory Management System

An internal Android application for inventory management using C66 devices with built-in RFID scanners.

## Features

### Core Features
- Real-time RFID tag scanning
- Automatic tag registration system
- Location-based inventory tracking
- Clean Material Design UI

### User Interface
- Independently scrollable tag lists
- One-tap data clearing
- Real-time scanning feedback
- Intuitive item registration

## Prerequisites

- Android Studio
- Java Development Kit (JDK)
- Node.js and npm (for backend)
- C66 device with built-in RFID scanner (for testing)
- Access to Yeahhost server

## Project Structure

```
rfid-proj/
├── app/                    # Android application module
│   ├── libs/              # External libraries
│   │   └── DeviceAPI.aar  # RFID scanner SDK
│   └── src/               # Source code
├── docs/                   # Documentation
├── Overview.md            # Project overview and architecture
├── Changelog.md           # Version history and changes
└── README.md             # Project setup and instructions
```

## Setup Instructions

1. Clone the repository
2. Open the project in Android Studio
3. Sync Gradle files
4. Build the project

## Development Status

### Completed Phases 
1. Basic UI Implementation
   - Material Design components
   - Core screens and navigation
   - Clean and simple styling

2. Backend Development
   - Express.js API setup
   - MySQL database integration
   - CRUD operations

3. RFID Integration
   - DeviceAPI SDK implementation
   - Hardware communication
   - Tag data processing

4. Frontend-Backend Integration
   - API endpoints integration
   - Real-time scanning
   - Error handling
   - Enhanced UX features

### Future Enhancements 
- Offline caching
- User authentication
- Additional features based on requirements

## Testing

1. UI Testing
   - Use Android Emulator
   - Test basic navigation and UI components

2. Hardware Testing
   - Build APK
   - Install on C66 device
   - Test RFID scanning functionality

## Database Configuration

Create a `.env` file in the root directory with the following variables:
```
DB_HOST=your_host
DB_PORT=your_port
DB_NAME=your_database
DB_USER=your_username
DB_PASSWORD=your_password
```

Note: Never commit the `.env` file to version control.

## Contributing

This is an internal project. Please follow the established coding guidelines and commit message format.

## Version History

See [Changelog.md](Changelog.md) for version history and changes.

## Project Documentation

See [Overview.md](Overview.md) for detailed project architecture and implementation details.
