variable "profile" {
  type = "string"
  description = "Name of your profile inside ~/.aws/credentials"
}

variable "region" {
  type = "string"
  description = "AWS Deployment Region"
  default = "us-west-2"
}
