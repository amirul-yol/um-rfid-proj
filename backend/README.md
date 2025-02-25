# RFID Inventory Backend

This is the backend server for the RFID Inventory Management System. It provides RESTful API endpoints for managing inventory items.

## Project Structure
```
backend/
├── src/
│   ├── config/
│   │   └── database.js     # Database configuration
│   ├── routes/
│   │   ├── index.js        # Route setup
│   │   └── inventory.routes.js  # Inventory endpoints
│   └── server.js           # Main server file
├── .env                    # Environment variables
├── package.json           # Project dependencies
└── README.md             # This file
```

## Setup Instructions

1. Navigate to the backend directory:
   ```bash
   cd backend
   ```

2. Change folder permission:
   ```bash
   chmod 600 .env
   ```

3. Install dependencies:
   ```bash
   npm install
   ```

4. Configure environment variables:
   - Copy `.env.example` to `.env`
   - Update the database credentials in `.env`

3. Start the server:
   ```bash
   # Development mode
   npm run dev

   # Production mode
   npm start
   ```

## API Endpoints

### Inventory Management

- `GET /api/v1/inventory` - Get all inventory items
- `GET /api/v1/inventory/:id` - Get a specific inventory item
- `POST /api/v1/inventory` - Create a new inventory item
- `PUT /api/v1/inventory/:id` - Update an inventory item
- `DELETE /api/v1/inventory/:id` - Delete an inventory item

### Health Check

- `GET /health` - Check server status

## Database Schema

### Inventory Table
```sql
CREATE TABLE inventory (
    id INT AUTO_INCREMENT PRIMARY KEY,
    rfid_tag VARCHAR(50) UNIQUE NOT NULL,
    name VARCHAR(100) NOT NULL,
    location VARCHAR(100),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);
```
