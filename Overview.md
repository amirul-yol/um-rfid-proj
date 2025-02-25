# RFID Inventory Management System

## Project Overview
An internal Android application for inventory management using C66 devices with built-in RFID scanners. The system consists of a Native Android app and Express.js backend connected to a MySQL database.

## System Architecture

### Android App (Native)
- Built for C66 devices with built-in RFID scanners
- Uses DeviceAPI SDK for RFID hardware integration
- Material Design UI components
- Real-time database synchronization

### Backend
- Express.js server
- MySQL database (hosted on Yeahhost server)
- RESTful API endpoints
- Real-time data synchronization

### Database
- MySQL database (hosted on Yeahhost server)
- Configuration via environment variables
- Accessible across different networks

## Core Features
1. RFID Scanning
   - Real-time tag reading
   - Hardware integration via DeviceAPI SDK

2. Inventory Management
   - Item tracking
   - Location management
   - Movement history

3. Database Synchronization
   - Real-time updates
   - Cross-network accessibility

## Development Approach

### Phase 1: Basic UI Implementation
- Material Design components
- Clean and simple styling
- Core screens:
  - RFID Scanner screen
  - Inventory list view
  - Item details view

### Phase 2: Backend Development
- Express.js API setup
- Database connection testing
- Basic CRUD operations

### Phase 3: RFID Integration
- SDK implementation
- Hardware communication
- Data processing

### Phase 4: Frontend-Backend Integration
- API integration
- Real-time sync implementation
- Error handling

### Future Enhancements
- Offline caching
- User authentication
- Additional features based on requirements

## Testing Strategy
1. UI Testing: Android Emulator
2. Hardware Testing: C66 Device APK
3. Backend Testing: API endpoints
4. Integration Testing: Full system
