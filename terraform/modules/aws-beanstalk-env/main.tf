data "aws_region" "current" { current = true }

resource "aws_elastic_beanstalk_application" "default" {
  name        = "${var.app-name}"
  description = "${var.app-description}"
}

resource "aws_iam_instance_profile" "default" {
  name = "${var.app-name}-eb-user"
  role = "${aws_iam_role.default.name}"
}

resource "aws_iam_role" "default" {
  name = "${var.app-name}-eb-role"
  assume_role_policy = <<EOF
{
  "Version": "2012-10-17",
  "Statement": [
    {
      "Action": "sts:AssumeRole",
      "Principal": {
        "Service": "ec2.amazonaws.com"
      },
      "Effect": "Allow",
      "Sid": ""
    }
  ]
}
EOF
}

# Overriding because by default Beanstalk does not have a permission to Read ECR
resource "aws_iam_role_policy" "default" {
  name = "${var.app-name}-eb-policy"
  role = "${aws_iam_role.default.id}"
  count = 0
  policy = <<EOF
{
  "Version": "2012-10-17",
  "Statement": [
    {
      "Action": [
        "cloudwatch:PutMetricData",
        "ds:CreateComputer",
        "ds:DescribeDirectories",
        "ec2:DescribeInstanceStatus",
        "logs:*",
        "ssm:*",
        "ec2messages:*",
        "s3:*"
      ],
      "Effect": "Allow",
      "Resource": "*"
    }
  ]
}
EOF
}
