# Dynamo repository service

This library is an attempt at creating a microservice that can function as a document folder data storage structure for dynamoDB.

Infomation will be structured in the form of Folders and documents, where there exists a hierarchical
relationship between individual items stored within a table. An individual entity stored witin the 
table is called a Node and a Node will consists off several properties.

Properties of a node can be business specific and can be built to satisfy any business needs.

### Api's Implemented:

#### REPO:
[ POST ] - /api/v1/repo/createnewrepo

Creates the Top level Node for a new project or a site. All Other nodes within the domain/project
/site will be the children of this master node.

#### NODE:
[GET | PUT | POST | DELETE] - /api/v1/node

These Api's allow performing Crud operations on a Node within dynamoDB.

### Planned's Api's

#### PROPERTY:
[GET| PUT | POST | DELETE] - /api/v1/property

API's to perform crud operations on individual properties of a Node.


#### CONTENT:

[GET| PUT | POST | DELETE] - /api/v1/content

API's to attach S3 content to a specific node.


## Running this project:

To be able to run this project you need to first create an instance of dynamoDB running locally
or have a table created within the AWS console.

The dynamodb table must be created with the following configuration:

    {
    TableName: <table_name>,
    KeySchema: [ 
        { 
            AttributeName: 'nodeId',
            KeyType: 'HASH',
        }
    ],
    AttributeDefinitions: [ 
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
    ProvisionedThroughput: { // Throughput values can be changed to desirable values
        ReadCapacityUnits: 10, 
        WriteCapacityUnits: 5, 
    },
    GlobalSecondaryIndexes: [ 
        { 
            IndexName: 'nodePath', 
            KeySchema: [
                { 
                    AttributeName: 'path',
                    KeyType: 'HASH',
                }
            ],
            Projection: { 
                ProjectionType: 'KEYS_ONLY', // (ALL | KEYS_ONLY | INCLUDE)
            },
            ProvisionedThroughput: { // Throughput values can be changed to desirable values
                ReadCapacityUnits: 10,
                WriteCapacityUnits: 5,
            },
        },
        {
            IndexName: 'parentNodeIndex',
            KeySchema: [
                { 
                    AttributeName: 'parentNodeId',
                    KeyType: 'HASH',
                }
            ],
            Projection: { 
                ProjectionType: 'KEYS_ONLY', 
            },
            ProvisionedThroughput: {
                ReadCapacityUnits: 10,
                WriteCapacityUnits: 5,
            },
        }
        ]
    }

Once the table has been created modify the 'application.properties' file under 
"src/main/resources" to add the table name, aws credentials and the URL endpoint for
dynamoDB.

Finally, run the Application.java class to start SpringBoot's inbuilt tomcat server.
