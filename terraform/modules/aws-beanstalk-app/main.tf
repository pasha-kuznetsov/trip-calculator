data "aws_region" "current" { current = true }

resource "aws_elastic_beanstalk_application_version" "default" {
  name        = "1.0-SNAPSHOT"
  application = "${var.app-name}"
  description = "Application Version managed by Terraform"
  bucket      = "${aws_s3_bucket.default.id}"
  key         = "${aws_s3_bucket_object.default.id}"
}

resource "aws_s3_bucket_object" "default" {
  bucket = "${aws_s3_bucket.default.id}"
  key    = "beanstalk/${var.app-name}.jar"
  source = "../../../build/libs/TripCalculator-1.0-SNAPSHOT.jar"
}

resource "aws_s3_bucket" "default" {
  bucket = "${var.app-fqn}-deployments"
}

resource "aws_elastic_beanstalk_environment" "default" {
  name                = "${var.app-name}-${var.app-env-name}"
  application         = "${var.app-name}"
  solution_stack_name = "64bit Amazon Linux 2017.03 v2.5.3 running Java 8"
  tier                = "WebServer"

  setting {
    namespace = "aws:autoscaling:asg"
    name      = "MaxSize"
    value     = "1"
  }

  setting {
    namespace = "aws:autoscaling:launchconfiguration"
    name      = "InstanceType"
    value     = "t2.micro"
  }

  setting {
    namespace = "aws:autoscaling:launchconfiguration"
    name      = "IamInstanceProfile"
    value     = "${var.app-name}-eb-user"
  }

  # EB listens on port 5000, adjust Spring config to bind to it instead of the default 8080
  setting {
    namespace = "aws:elasticbeanstalk:application:environment"
    name      = "SERVER_PORT"
    value     = "5000"
  }
}
