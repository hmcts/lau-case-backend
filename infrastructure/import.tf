import {
  to = module.lau-case-db-flexible.azurerm_postgresql_flexible_server.pgsql_server
  id = "/subscriptions/8999dec3-0104-4a27-94ee-6588559729d1/resourceGroups/lau-case-backend-flexible-data-prod/providers/Microsoft.DBforPostgreSQL/flexibleServers/lau-case-backend-flexible-prod"
}

import {
  to = module.lau-case-db-flexible.azurerm_postgresql_flexible_server_database.pg_databases["lau_case"]
  id = "/subscriptions/8999dec3-0104-4a27-94ee-6588559729d1/resourceGroups/lau-case-backend-flexible-data-prod/providers/Microsoft.DBforPostgreSQL/flexibleServers/lau-case-backend-flexible-prod/databases/lau_case"
}

import {
  to = module.lau-case-db-flexible.azurerm_postgresql_flexible_server_configuration.pgsql_server_config["azure.extensions"]
  id = "/subscriptions/8999dec3-0104-4a27-94ee-6588559729d1/resourceGroups/lau-case-backend-flexible-data-prod/providers/Microsoft.DBforPostgreSQL/flexibleServers/lau-case-backend-flexible-prod/configurations/azure.extensions"
}

