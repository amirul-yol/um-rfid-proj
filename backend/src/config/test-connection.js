const path = require('path');
require('dotenv').config({ path: path.join(__dirname, '../../.env') });
const mysql = require('mysql2');

// Create connection with more detailed error handling
const connection = mysql.createConnection({
  host: process.env.DB_HOST,
  port: process.env.DB_PORT,
  user: process.env.DB_USER,
  password: process.env.DB_PASSWORD,
  database: process.env.DB_NAME,
  connectTimeout: 10000, // 10 seconds
  debug: true // Enable debugging
});

console.log('Attempting to connect to database...');
console.log(`Host: ${process.env.DB_HOST}`);
console.log(`Port: ${process.env.DB_PORT}`);
console.log(`User: ${process.env.DB_USER}`);
console.log(`Database: ${process.env.DB_NAME}`);

connection.connect((err) => {
  if (err) {
    console.error('Error connecting to the database:');
    console.error(`Error code: ${err.code}`);
    console.error(`Error number: ${err.errno}`);
    console.error(`SQL State: ${err.sqlState}`);
    console.error('Full error:', err);
    process.exit(1);
  }

  console.log('Successfully connected to database!');
  
  // Try a simple query
  connection.query('SELECT 1 + 1 AS solution', (err, results) => {
    if (err) {
      console.error('Error running test query:', err);
    } else {
      console.log('Test query result:', results);
    }
    
    connection.end((err) => {
      if (err) {
        console.error('Error closing connection:', err);
      }
      process.exit(0);
    });
  });
});
