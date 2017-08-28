provider "aws" {
    profile = "${var.profile}"
    region  = "${var.region}"
}

module "trip-calc-env" {
    source = "../../modules/aws-beanstalk-env"
}
