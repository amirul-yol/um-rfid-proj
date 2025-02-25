const { getConnection } = require('../config/database');

class InventoryService {
  async getAllItems() {
    const connection = getConnection();
    const [rows] = await connection.query(`
      SELECT i.*, 
             COUNT(m.id) as movement_count,
             MAX(m.timestamp) as last_movement
      FROM items i
      LEFT JOIN movements m ON i.id = m.item_id
      GROUP BY i.id
      ORDER BY i.created_at DESC
    `);
    return rows;
  }

  async getItemById(id) {
    const connection = getConnection();
    const [rows] = await connection.query(`
      SELECT i.*, 
             COUNT(m.id) as movement_count,
             MAX(m.timestamp) as last_movement
      FROM items i
      LEFT JOIN movements m ON i.id = m.item_id
      WHERE i.id = ?
      GROUP BY i.id
    `, [id]);
    
    if (rows.length === 0) {
      return null;
    }

    // Get movement history
    const [history] = await connection.query(`
      SELECT m.*, 
             l1.name as from_location_name,
             l2.name as to_location_name
      FROM movements m
      LEFT JOIN locations l1 ON m.from_location = l1.id
      LEFT JOIN locations l2 ON m.to_location = l2.id
      WHERE item_id = ?
      ORDER BY timestamp DESC
      LIMIT 10
    `, [id]);

    return {
      ...rows[0],
      movement_history: history
    };
  }

  async getItemByRfidTag(rfidTag) {
    const connection = getConnection();
    const [rows] = await connection.query(`
      SELECT i.*, l.name as location_name
      FROM items i
      LEFT JOIN locations l ON i.current_location = l.id
      WHERE i.rfid_tag = ?
    `, [rfidTag]);
    
    if (rows.length === 0) {
      return null;
    }

    return {
      id: rows[0].id,
      name: rows[0].name,
      rfid_tag: rows[0].rfid_tag,
      location: {
        id: rows[0].current_location,
        name: rows[0].location_name
      }
    };
  }

  async createItem(item) {
    const connection = getConnection();
    const [result] = await connection.query(
      'INSERT INTO items (name, rfid_tag, description, current_location) VALUES (?, ?, ?, ?)',
      [item.name, item.rfid_tag, item.description, item.current_location]
    );
    return this.getItemById(result.insertId);
  }

  async updateItem(id, item) {
    const connection = getConnection();
    await connection.query(
      'UPDATE items SET name = ?, rfid_tag = ?, description = ?, current_location = ? WHERE id = ?',
      [item.name, item.rfid_tag, item.description, item.current_location, id]
    );
    return this.getItemById(id);
  }

  async deleteItem(id) {
    const connection = getConnection();
    await connection.query('DELETE FROM movements WHERE item_id = ?', [id]);
    await connection.query('DELETE FROM items WHERE id = ?', [id]);
  }

  async recordMovement(itemId, fromLocation, toLocation) {
    const connection = getConnection();
    
    // Insert movement record
    await connection.query(
      'INSERT INTO movements (item_id, from_location, to_location) VALUES (?, ?, ?)',
      [itemId, fromLocation, toLocation]
    );

    // Update item's current location
    await connection.query(
      'UPDATE items SET current_location = ? WHERE id = ?',
      [toLocation, itemId]
    );

    return this.getItemById(itemId);
  }
}

module.exports = new InventoryService();
