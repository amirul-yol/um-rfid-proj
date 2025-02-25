const express = require('express');
const router = express.Router();
const inventoryService = require('../services/inventory.service');

// Get all inventory items
router.get('/', async (req, res, next) => {
  try {
    const items = await inventoryService.getAllItems();
    res.json(items);
  } catch (error) {
    next(error);
  }
});

// Get a single inventory item by ID
router.get('/:id', async (req, res, next) => {
  try {
    const item = await inventoryService.getItemById(req.params.id);
    
    if (!item) {
      return res.status(404).json({ message: 'Item not found' });
    }
    
    res.json(item);
  } catch (error) {
    next(error);
  }
});

// Get item by RFID tag
router.get('/tag/:rfidTag', async (req, res, next) => {
  try {
    const item = await inventoryService.getItemByRfidTag(req.params.rfidTag);
    
    if (!item) {
      return res.status(404).json({ message: 'Item not found' });
    }
    
    res.json(item);
  } catch (error) {
    next(error);
  }
});

// Create a new inventory item
router.post('/', async (req, res, next) => {
  try {
    const newItem = await inventoryService.createItem(req.body);
    res.status(201).json(newItem);
  } catch (error) {
    if (error.code === 'ER_DUP_ENTRY') {
      return res.status(400).json({ message: 'RFID tag already exists' });
    }
    next(error);
  }
});

// Update an inventory item
router.put('/:id', async (req, res, next) => {
  try {
    const updatedItem = await inventoryService.updateItem(req.params.id, req.body);
    
    if (!updatedItem) {
      return res.status(404).json({ message: 'Item not found' });
    }
    
    res.json(updatedItem);
  } catch (error) {
    if (error.code === 'ER_DUP_ENTRY') {
      return res.status(400).json({ message: 'RFID tag already exists' });
    }
    next(error);
  }
});

// Delete an inventory item
router.delete('/:id', async (req, res, next) => {
  try {
    const deleted = await inventoryService.deleteItem(req.params.id);
    
    if (!deleted) {
      return res.status(404).json({ message: 'Item not found' });
    }
    
    res.json({ message: 'Item deleted successfully' });
  } catch (error) {
    next(error);
  }
});

// Record item movement
router.post('/:id/movement', async (req, res, next) => {
  try {
    const { from_location, to_location } = req.body;
    const updatedItem = await inventoryService.recordMovement(
      req.params.id,
      from_location,
      to_location
    );
    
    if (!updatedItem) {
      return res.status(404).json({ message: 'Item not found' });
    }
    
    res.json(updatedItem);
  } catch (error) {
    next(error);
  }
});

module.exports = router;
