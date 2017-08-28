provider "aws" {
    profile = "${var.profile}"
    region  = "${var.region}"
}

module "trip-calc-app" {
    source = "../../modules/aws-beanstalk-app"
    app-fqn = "${basename(path.module)}"
}
