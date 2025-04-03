variable "product" {
  default = "lau"
}

variable "component" {
  default = "case-backend"
}

variable "location" {
  default = "UK South"
}

variable "location_db" {
  default = "UK South"
}

variable "env" {
}

variable "tenant_id" {
  description = "(Required) The Azure Active Directory tenant ID that should be used for authenticating requests to the key vault. This is usually sourced from environemnt variables and not normally required to be specified."
}

variable "jenkins_AAD_objectId" {
  description = "(Required) The Azure AD object ID of a user, service principal or security group in the Azure Active Directory tenant for the vault. The object ID must be unique for the list of access policies."
}

variable "subscription" {
}

variable "common_tags" {
  type = map(string)
}

variable "appinsights_location" {
  default     = "West Europe"
  description = "Location for Application Insights"
}

variable "appinsights_instrumentation_key" {
  description = "Instrumentation key of the App Insights instance this webapp should use. Module will create own App Insights resource if this is not provided"
  default     = ""
}

variable "lau_case_db_name" {
  description = "Name of database to use"
  default     = "lau_case"
}

variable "aks_subscription_id" {}

variable "postgresql_flexible_server_port" {
  default = "5432"
}

variable "pgsql_storage_mb" {
  type        = number
  description = "Flexible Postgres DB size in mb"
  default     = 131072
}

variable "subnet_suffix" {
  default     = null
  type        = string
  description = "Suffix to append to the subnet name, the originally created one used by this module is full in a number of environments."
}

variable "db_monitor_action_group_name" {
  description = "The name of the Action Group to create."
  type        = string
  default     = "db_monitor_ag"
}

variable "db_alert_email_address_key" {
  description = "Email address key in azure Key Vault."
  type        = string
  default     = "caseDisposerAlertEmail"
}
