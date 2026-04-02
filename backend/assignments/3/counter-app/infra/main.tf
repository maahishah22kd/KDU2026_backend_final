provider "aws" {
  region = var.region
}

# We will IMPORT your manually created bucket into this resource
resource "aws_s3_bucket" "frontend" {
  bucket = var.frontend_bucket_name
  tags   = var.tags
}


# Website hosting configuration
resource "aws_s3_bucket_website_configuration" "frontend" {
  bucket = aws_s3_bucket.frontend.id

  index_document {
    suffix = "index.html"
  }
}

# Allow bucket policy to make objects public (needed for S3 website hosting)
resource "aws_s3_bucket_public_access_block" "frontend" {
  bucket = aws_s3_bucket.frontend.id

  block_public_acls       = false
  block_public_policy     = false
  ignore_public_acls      = false
  restrict_public_buckets = false
}

# Public read policy (GET objects only)
data "aws_iam_policy_document" "frontend_public_read" {
  statement {
    sid     = "PublicReadGetObject"
    effect  = "Allow"
    actions = ["s3:GetObject"]

    resources = ["${aws_s3_bucket.frontend.arn}/*"]

    principals {
      type        = "AWS"
      identifiers = ["*"]
    }
  }
}

resource "aws_s3_bucket_policy" "frontend" {
  bucket = aws_s3_bucket.frontend.id
  policy = data.aws_iam_policy_document.frontend_public_read.json

  depends_on = [aws_s3_bucket_public_access_block.frontend]
}

# Upload index.html via Terraform
resource "aws_s3_object" "index_html" {
  bucket       = aws_s3_bucket.frontend.id
  key          = "index.html"
  source       = "${path.module}/../frontend/index.html"
  content_type = "text/html"
  etag         = filemd5("${path.module}/../frontend/index.html")
}

# Upload index.js via Terraform
resource "aws_s3_object" "index_js" {
  bucket = aws_s3_bucket.frontend.id
  key    = "index.js"
  content = templatefile("${path.module}/../frontend/index.js.tftpl", {
    api_base_url = aws_apigatewayv2_stage.prod.invoke_url
  })
  content_type = "application/javascript"
}


# ----------------------------
# DynamoDB (Counter storage)
# ----------------------------
resource "aws_dynamodb_table" "counter" {
  name         = "${var.frontend_bucket_name}-counter"
  billing_mode = "PAY_PER_REQUEST"
  hash_key     = "counterId"

  attribute {
    name = "counterId"
    type = "S"
  }

  tags = var.tags
}

# ----------------------------
# Lambda packaging (zip)
# ----------------------------
data "archive_file" "lambda_zip" {
  type        = "zip"
  source_file = "${path.module}/../lambda/index.mjs"
  output_path = "${path.module}/../lambda/function.zip"
}

# ----------------------------
# IAM for Lambda (least privilege)
# ----------------------------
resource "aws_iam_role" "lambda_role" {
  name = "${var.frontend_bucket_name}-counter-lambda-role"

  assume_role_policy = jsonencode({
    Version = "2012-10-17",
    Statement = [{
      Effect    = "Allow",
      Action    = "sts:AssumeRole",
      Principal = { Service = "lambda.amazonaws.com" }
    }]
  })

  tags = var.tags
}

data "aws_iam_policy_document" "lambda_policy" {
  statement {
    effect = "Allow"
    actions = [
      "dynamodb:GetItem",
      "dynamodb:UpdateItem"
    ]
    resources = [aws_dynamodb_table.counter.arn]
  }

  statement {
    effect = "Allow"
    actions = [
      "logs:CreateLogGroup",
      "logs:CreateLogStream",
      "logs:PutLogEvents"
    ]
    resources = ["*"]
  }
}

resource "aws_iam_policy" "lambda_policy" {
  name   = "${var.frontend_bucket_name}-counter-lambda-policy"
  policy = data.aws_iam_policy_document.lambda_policy.json
}

resource "aws_iam_role_policy_attachment" "lambda_attach" {
  role       = aws_iam_role.lambda_role.name
  policy_arn = aws_iam_policy.lambda_policy.arn
}

# ----------------------------
# Lambda function
# ----------------------------
resource "aws_lambda_function" "counter" {
  function_name = "${var.frontend_bucket_name}-counter"
  role          = aws_iam_role.lambda_role.arn

  runtime = "nodejs20.x"
  handler = "index.handler"

  filename         = data.archive_file.lambda_zip.output_path
  source_code_hash = data.archive_file.lambda_zip.output_base64sha256

  environment {
    variables = {
      TABLE_NAME = aws_dynamodb_table.counter.name
      COUNTER_ID = "main"
    }
  }

  tags = var.tags
}



resource "aws_apigatewayv2_api" "counter_api" {
  name          = "${var.frontend_bucket_name}-api"
  protocol_type = "HTTP"

  cors_configuration {
    allow_origins = ["*"]
    allow_methods = ["GET", "PUT", "OPTIONS"]
    allow_headers = ["content-type"]
  }

  tags = var.tags
}

resource "aws_apigatewayv2_integration" "lambda" {
  api_id           = aws_apigatewayv2_api.counter_api.id
  integration_type = "AWS_PROXY"
  integration_uri  = aws_lambda_function.counter.invoke_arn
}

resource "aws_apigatewayv2_route" "get_counter" {
  api_id    = aws_apigatewayv2_api.counter_api.id
  route_key = "GET /counter"
  target    = "integrations/${aws_apigatewayv2_integration.lambda.id}"
}

resource "aws_apigatewayv2_route" "put_counter" {
  api_id    = aws_apigatewayv2_api.counter_api.id
  route_key = "PUT /counter"
  target    = "integrations/${aws_apigatewayv2_integration.lambda.id}"
}

resource "aws_apigatewayv2_stage" "prod" {
  api_id      = aws_apigatewayv2_api.counter_api.id
  name        = "prod"
  auto_deploy = true

  tags = var.tags
}



resource "aws_lambda_permission" "api_invoke" {
  statement_id  = "AllowAPIGatewayInvoke"
  action        = "lambda:InvokeFunction"
  function_name = aws_lambda_function.counter.function_name
  principal     = "apigateway.amazonaws.com"
  source_arn    = "${aws_apigatewayv2_api.counter_api.execution_arn}/*/*"
}


