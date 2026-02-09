import { DynamoDBClient } from "@aws-sdk/client-dynamodb";
import { DynamoDBDocumentClient, GetCommand, UpdateCommand } from "@aws-sdk/lib-dynamodb";

const ddb = DynamoDBDocumentClient.from(new DynamoDBClient({}));

const TABLE_NAME = process.env.TABLE_NAME;
const COUNTER_ID = process.env.COUNTER_ID || "main";

export const handler = async (event) => {
  const method = event?.requestContext?.http?.method || event?.httpMethod || "TEST";

  try {
    if (method === "GET") {
      const res = await ddb.send(new GetCommand({
        TableName: TABLE_NAME,
        Key: { counterId: COUNTER_ID },
      }));

      const value = res?.Item?.value ?? 0;

      return {
        statusCode: 200,
        headers: { "content-type": "application/json" },
        body: JSON.stringify({ value }),
      };
    }

    if (method === "PUT") {
      // Atomic increment
      const res = await ddb.send(new UpdateCommand({
        TableName: TABLE_NAME,
        Key: { counterId: COUNTER_ID },
        UpdateExpression: "ADD #v :inc",
        ExpressionAttributeNames: { "#v": "value" },
        ExpressionAttributeValues: { ":inc": 1 },
        ReturnValues: "UPDATED_NEW",
      }));

      const value = res?.Attributes?.value ?? 0;

      return {
        statusCode: 200,
        headers: { "content-type": "application/json" },
        body: JSON.stringify({ value }),
      };
    }

    return {
      statusCode: 405,
      headers: { "content-type": "application/json" },
      body: JSON.stringify({ message: "Method not allowed" }),
    };
  } catch (err) {
    console.error(err);
    return {
      statusCode: 500,
      headers: { "content-type": "application/json" },
      body: JSON.stringify({ message: "Internal error" }),
    };
  }
};