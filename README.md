# AWSOfferings

AWSOfferings is an AWS Lambda function front-ended by Amazon API Gateway that 
can retrieve a list of AWS regions that offer all the services listed in a 
query string parameter named services.  

## Purpose

Given a set of AWS services needed to address a particular customer's needs, 
the attached AWS Lambda function and Amazon API Gateway provide a list of 
regions that support that set of AWS services.

## Examples

### Services Supported

To determine all the AWS offerings this Lambda / API Gateway is able to handle,
issue the following GET request:

```
https://xcvmkf8oqc.execute-api.us-east-1.amazonaws.com/test/%7Bproxy+%7D
```

Postman will return the following results indicating all the AWS offerings 
this Lambda / API Gateway is able to handle. Please note that no AWS region 
supports all AWS offerings:

```
{
    "regions": [],
    "services": [
        "a4b",
        "acm",
        "acm-pca",
        "api.mediatailor",
        "api.pricing",
        "apigateway",
        "appstream2",
        "athena",
        "autoscaling",
        "batch",
        "budgets",
        "ce",
        "ce.us-east-1",
        "cloud9",
        "clouddirectory",
        "cloudformation",
        "cloudfront",
        "cloudhsm",
        "cloudhsmv2",
        "cloudsearch",
        "cloudtrail",
        "codebuild",
        "codecommit",
        "codedeploy",
        "codepipeline",
        "codestar",
        "cognito-identity",
        "cognito-idp",
        "cognito-sync",
        "comprehend",
        "config",
        "cur",
        "data.iot",
        "datapipeline",
        "dax",
        "devicefarm",
        "directconnect",
        "discovery",
        "dms",
        "ds",
        "dynamodb",
        "ec2",
        "ecr",
        "ecs",
        "elasticache",
        "elasticbeanstalk",
        "elasticfilesystem",
        "elasticloadbalancing",
        "elasticmapreduce",
        "elastictranscoder",
        "email",
        "entitlement.marketplace",
        "es",
        "events",
        "firehose",
        "fms",
        "gamelift",
        "glacier",
        "glue",
        "greengrass",
        "guardduty",
        "health",
        "iam",
        "iam.cn-north-1",
        "iam.us-gov",
        "importexport",
        "inspector",
        "iot",
        "kinesis",
        "kinesisanalytics",
        "kinesisvideo",
        "kms",
        "lambda",
        "lightsail",
        "logs",
        "machinelearning",
        "marketplacecommerceanalytics",
        "mediaconvert",
        "medialive",
        "mediapackage",
        "mediastore",
        "metering.marketplace",
        "mgh",
        "mobileanalytics",
        "models.lex",
        "monitoring",
        "mturk-requester",
        "opsworks",
        "opsworks-cm",
        "organizations",
        "organizations.us-east-1",
        "pinpoint",
        "polly",
        "rds",
        "redshift",
        "rekognition",
        "resource-groups",
        "route53",
        "route53domains",
        "runtime.lex",
        "runtime.sagemaker",
        "s3",
        "sagemaker",
        "sdb",
        "secretsmanager",
        "serverlessrepo",
        "servicecatalog",
        "servicediscovery",
        "shield",
        "sms",
        "snowball",
        "sns",
        "sqs",
        "ssm",
        "states",
        "storagegateway",
        "streams.dynamodb",
        "sts",
        "support",
        "swf",
        "tagging",
        "translate",
        "waf",
        "waf-regional",
        "workdocs",
        "workmail",
        "workspaces",
        "xray"
    ]
}
```

### A Simple Example

To retrieve a list of all regions that support 
Amazon Simple Storage Service (S3), 
issue the following GET request:

```
https://xcvmkf8oqc.execute-api.us-east-1.amazonaws.com/test/%7Bproxy+%7D?services=S3
```

Postman will return the following results indicating all regions that 
support S3:

```
{
    "regions": [
        "ap-northeast-1",
        "ap-northeast-2",
        "ap-south-1",
        "ap-southeast-1",
        "ap-southeast-2",
        "ca-central-1",
        "cn-north-1",
        "cn-northwest-1",
        "eu-central-1",
        "eu-west-1",
        "eu-west-2",
        "eu-west-3",
        "sa-east-1",
        "us-east-1",
        "us-east-2",
        "us-gov-west-1",
        "us-west-1",
        "us-west-2"
    ],
    "services": [
        "s3"
    ]
}
```

### A More Complex Example

To retrieve a list of all regions that support 
Amazon Simple Storage Service (S3) and
Amazon AppStream 2.0, 
issue the following GET request:

```
https://xcvmkf8oqc.execute-api.us-east-1.amazonaws.com/test/%7Bproxy+%7D?services=S3,appstream2
```

Postman will return the following results indicating all regions that 
support S3 and AppStream 2.0:

```
{
    "regions": [
        "ap-northeast-1",
        "eu-west-1",
        "us-east-1",
        "us-west-2"
    ],
    "services": [
        "appstream2",
        "s3"
    ]
}
```

## Why

Although the <a href="https://aws.amazon.com/about-aws/global-infrastructure/regional-product-services/">
AWS Region Table</a> can be used for a similar purpose, the AWS Region Table 
becomes difficult to use as more and more services are provisioned to meet
a customer's needs.