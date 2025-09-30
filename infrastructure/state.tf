terraform {
  backend "azurerm" {}
  required_providers {
    azuread = {
      source  = "hashicorp/azuread"
      version = "3.6.0"
    }
  }
}
