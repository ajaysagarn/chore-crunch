var params = {
    TableName: 'chore_crunch',
    KeySchema: [ // The type of of schema.  Must start with a HASH type, with an optional second RANGE.
        { // Required HASH type attribute
            AttributeName: 'nodeId',
            KeyType: 'HASH',
        }
    ],
    AttributeDefinitions: [ // The names and types of all primary and index key attributes only
        {
            AttributeName: 'nodeId',
            AttributeType: 'S', // (S | N | B) for string, number, binary
        },
        {
            AttributeName: 'path',
            AttributeType: 'S', // (S | N | B) for string, number, binary
        },
        {
            AttributeName: 'parentNodeId',
            AttributeType: 'S', // (S | N | B) for string, number, binary
        }// ... more attributes ...
    ],
    ProvisionedThroughput: { // required provisioned throughput for the table
        ReadCapacityUnits: 10, 
        WriteCapacityUnits: 5, 
    },
    GlobalSecondaryIndexes: [ // optional (list of GlobalSecondaryIndex)
        { 
            IndexName: 'nodePath', 
            KeySchema: [
                { // Required HASH type attribute
                    AttributeName: 'path',
                    KeyType: 'HASH',
                }
            ],
            Projection: { // attributes to project into the index
                ProjectionType: 'KEYS_ONLY', // (ALL | KEYS_ONLY | INCLUDE)
            },
            ProvisionedThroughput: { // throughput to provision to the index
                ReadCapacityUnits: 10,
                WriteCapacityUnits: 5,
            },
        },
        {
            IndexName: 'parentNodeIndex',
            KeySchema: [
                { // Required HASH type attribute
                    AttributeName: 'parentNodeId',
                    KeyType: 'HASH',
                }
            ],
            Projection: { // attributes to project into the index
                ProjectionType: 'KEYS_ONLY', // (ALL | KEYS_ONLY | INCLUDE)
            },
            ProvisionedThroughput: { // throughput to provision to the index
                ReadCapacityUnits: 10,
                WriteCapacityUnits: 5,
            },
        }
        // ... more global secondary indexes ...
    ]
};
dynamodb.createTable(params, function(err, data) {

})
  
  
  