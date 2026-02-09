variable "region" {
  type    = string
  default = "ap-south-1"
}

variable "frontend_bucket_name" {
  type = string
}

variable "tags" {
  type = map(string)
  default = {
    Project = "serverless-counter"
  }
}