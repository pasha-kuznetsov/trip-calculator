variable "app-name" {
  type = "string"
  description = "Application Name"
  default = "trip-calc"
}

variable "app-fqn" {
  type = "string"
  description = "Application Name Suffix, e.g. trip-calc-test.us-west-2.tydapps.com"
}

variable "app-description" {
  type = "string"
  description = "Application Description"
  default = "Trip Calculator Elastic Beanstalk App"
}

variable "app-env-name" {
  type = "string"
  description = "Application Environment Name, e.g. 'staging', 'production', 'test', 'integration'"
  default = "test"
}
