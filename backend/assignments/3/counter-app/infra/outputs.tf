output "frontend_bucket_name" {
  value = aws_s3_bucket.frontend.bucket
}

output "s3_website_endpoint" {
  value = "http://${aws_s3_bucket_website_configuration.frontend.website_endpoint}"
}

output "dynamodb_table_name" {
  value = aws_dynamodb_table.counter.name
}

output "lambda_function_name" {
  value = aws_lambda_function.counter.function_name
}

output "api_base_url" {
  value = aws_apigatewayv2_stage.prod.invoke_url
}