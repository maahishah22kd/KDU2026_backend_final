terraform {
  required_version = ">= 1.5.0"

  backend "s3" {
    bucket         = "maahi-counter-terraform-bucket"
    key            = "counter-app/terraform.tfstate"
    region         = "ap-south-1"
    dynamodb_table = "terrafrom-locks-maahi"
    encrypt        = true
  }

  required_providers {
    aws = {
      source  = "hashicorp/aws"
      version = "~> 5.0"
    }
    archive = {
      source  = "hashicorp/archive"
      version = "~> 2.5"
    }
  }
}