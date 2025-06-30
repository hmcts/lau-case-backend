# Demo environment imports - only active when env=demo
import {
  to = var.env == "demo" ? module.lau-case-db-flexible.azurerm_postgresql_flexible_server.pgsql_server : null
  id = "/subscriptions/1c4f0704-a29e-403d-b719-b90c34ef14c9/resourceGroups/lau-case-backend-flexible-data-demo/providers/Microsoft.DBforPostgreSQL/flexibleServers/lau-case-backend-flexible-demo"
}

import {
  to = var.env == "demo" ? module.lau-case-db-flexible.azurerm_postgresql_flexible_server_database.pg_databases["lau_case"] : null
  id = "/subscriptions/1c4f0704-a29e-403d-b719-b90c34ef14c9/resourceGroups/lau-case-backend-flexible-data-demo/providers/Microsoft.DBforPostgreSQL/flexibleServers/lau-case-backend-flexible-demo/databases/lau_case"
}

import {
  to = var.env == "demo" ? module.lau-case-db-flexible.azurerm_postgresql_flexible_server_configuration.pgsql_server_config["azure.extensions"] : null
  id = "/subscriptions/1c4f0704-a29e-403d-b719-b90c34ef14c9/resourceGroups/lau-case-backend-flexible-data-demo/providers/Microsoft.DBforPostgreSQL/flexibleServers/lau-case-backend-flexible-demo/configurations/azure.extensions"
}
