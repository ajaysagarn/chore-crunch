package com.chore.crunch.repository.dao;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBDeleteExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapperConfig;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBSaveExpression;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.ExpectedAttributeValue;
import com.chore.crunch.repository.model.Node;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class NodeDaoImpl implements NodeDao{

    private DynamoDBMapper mapper;

/*    @Value("${amazon.table}")
    private String table;*/

    @Autowired
    public NodeDaoImpl(DynamoDBMapper mapper){
        this.mapper = mapper;
    }

    @Override
    public Node createNode(Node node) {
       mapper.save(node);
       return node;
    }

    @Override
    public Node readNode(String nodeId) {
        return mapper.load(Node.class, nodeId);
    }

    @Override
    public Node updateNode(Node node) {
        Map<String, ExpectedAttributeValue> expectedAttributeValueMap = new HashMap<>();
        expectedAttributeValueMap.put("nodeId", new ExpectedAttributeValue(new AttributeValue().withS(node.getNodeId())));
        DynamoDBSaveExpression saveExpression = new DynamoDBSaveExpression().withExpected(expectedAttributeValueMap);
        mapper.save(node, saveExpression);
        return node;
    }

    @Override
    public void deleteNode(String nodeId) {
        Map<String, ExpectedAttributeValue> expectedAttributeValueMap = new HashMap<>();
        expectedAttributeValueMap.put("nodeId", new ExpectedAttributeValue(new AttributeValue().withS(nodeId)));
        DynamoDBDeleteExpression deleteExpression = new DynamoDBDeleteExpression().withExpected(expectedAttributeValueMap);
        Node user = Node.builder()
                .nodeId(nodeId)
                .build();
        mapper.delete(user, deleteExpression);
    }
}
