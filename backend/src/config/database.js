const mysql = require('mysql2/promise');

let connection = null;

async function connectToDatabase() {
  try {
    connection = await mysql.createConnection({
      host: process.env.DB_HOST,
      port: process.env.DB_PORT,
      user: process.env.DB_USER,
      password: process.env.DB_PASSWORD,
      database: process.env.DB_NAME
    });

    // Test the connection
    await connection.query('SELECT 1');
    console.log('Database connection successful');
    
    return connection;
  } catch (error) {
    console.error('Database connection failed:', error);
    throw error;
  }
}

function getConnection() {
  if (!connection) {
    throw new Error('Database connection not initialized');
  }
  return connection;
}

module.exports = {
  connectToDatabase,
  getConnection
};
