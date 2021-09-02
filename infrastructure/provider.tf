provider "azurerm" {
  features {}
  alias           = "aks"
  subscription_id = var.aks_subscription_id
  skip_provider_registration = true
}
