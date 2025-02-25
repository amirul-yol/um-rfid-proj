const inventoryRoutes = require('./inventory.routes');

function setupRoutes(app) {
  // API version prefix
  const apiPrefix = '/api/v1';
  
  // Register route modules
  app.use(`${apiPrefix}/inventory`, inventoryRoutes);
}

module.exports = {
  setupRoutes
};
