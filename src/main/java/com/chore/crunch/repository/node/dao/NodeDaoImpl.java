package com.chore.crunch.repository.node.dao;

import com.amazonaws.services.dynamodbv2.datamodeling.*;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.ExpectedAttributeValue;
import com.chore.crunch.repository.node.model.Node;
import com.chore.crunch.repository.node.model.NodeResponse;
import com.chore.crunch.util.RandomIdGenerator;
import com.chore.crunch.util.RepositoryException;
import com.chore.crunch.util.RepositoryUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toCollection;

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
    public Optional<NodeResponse> readNode(String nodeId, Integer levels) throws RepositoryException {

        boolean isNodeId = RepositoryUtils.isNodePathNodeId(nodeId);

        if(isNodeId){
            NodeResponse node = mapper.load(NodeResponse.class, nodeId);
            if(levels > 0)
                node.setChildren(getNodeChildren(node.getNodeId(),levels));
            return Optional.ofNullable(node);
        }else{
            Map<String, AttributeValue> eav = new HashMap<String, AttributeValue>();
            eav.put(":val1", new AttributeValue().withS(nodeId));
            Map<String, String> ean = new HashMap<String, String>();
            ean.put("#name", "path");

            DynamoDBQueryExpression<Node> queryExpression = new DynamoDBQueryExpression<Node>()
                    .withIndexName("nodePath")
                    .withKeyConditionExpression("#name= :val1").withExpressionAttributeNames(ean)
                    .withExpressionAttributeValues(eav)
                    .withConsistentRead(false).withLimit(1);

            List<Node> nodes =  mapper.query(Node.class, queryExpression);
            if(nodes.isEmpty()){
                throw new RepositoryException(HttpStatus.NOT_FOUND.value(), "node with given path does not exist.");
            }

            return readNode(nodes.get(0).getNodeId(),levels);
        }
    }

    private ArrayList<Node> getNodeChildren(String nodeId, Integer levels) {
        ArrayList<Node> children = new ArrayList<Node>();
        List<String> nodeIds = new ArrayList<String>();

        nodeIds = getChildNodeIds(nodeId,levels, false);

        if(!nodeIds.isEmpty()){
            children = (ArrayList<Node>) batchLoadNodes(nodeIds);
        }
        return children;
    }

    public List<String> getChildNodeIds(String parentNodeId, Integer levels, boolean fetchAll){
        Map<String, AttributeValue> eav = new HashMap<String, AttributeValue>();
        eav.put(":val1", new AttributeValue().withS(parentNodeId));

        DynamoDBQueryExpression<Node> queryExpression = new DynamoDBQueryExpression<Node>()
                .withIndexName("parentNodeIndex")
                .withKeyConditionExpression("parentNodeId= :val1")
                .withExpressionAttributeValues(eav)
                .withConsistentRead(false);

        List<Node> nodes =  mapper.query(Node.class, queryExpression);
        List<String> nodeIds = nodes.stream().map(node -> node.getNodeId()).collect(Collectors.toList());

        List<String> levelNodeIds = new ArrayList<>(nodeIds);
        levels = levels - 1;

        if(!levelNodeIds.isEmpty() && (levels > 0 || fetchAll)){
            for (String nodeId: levelNodeIds) {
                nodeIds.addAll(getChildNodeIds(nodeId, levels, fetchAll));
            }
        }

        return nodeIds;
    }




    public List<Node> batchLoadNodes(List<String> nodeIds) {

        List<KeyPair> keyPairList = new ArrayList<>();

        for (String nodeId:nodeIds) {
            KeyPair keyPair = new KeyPair();
            keyPair.withHashKey(nodeId);
            keyPairList.add(keyPair);
        }

        Map<Class<?>, List<KeyPair>> keyPairForTable = new HashMap<>();
        keyPairForTable.put(Node.class, keyPairList);

        Map<String, List<Object>> batchResults = mapper.batchLoad(keyPairForTable);

        List<Node> childNodes = new ArrayList<Node>();
        for (Map.Entry<String, List<Object>> entry : batchResults.entrySet()) {
            System.out.println(entry.getKey());
            System.out.println(entry.getValue());
            childNodes.addAll(entry.getValue().stream().map(e -> (Node)e).collect(Collectors.toList()));
        }
        return childNodes;

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
    public Node createRepo() {
        Node repo = new Node();
        repo.setNodeId(RandomIdGenerator.getRandomNodeId());
        repo.setNodeType(Node.NodeType.REPO.toString());
        repo.setPath(repo.getNodeId());
        mapper.save(repo);
        return repo;
    }

    @Override
    public void deleteNode(String nodeId) {
        Map<String, ExpectedAttributeValue> expectedAttributeValueMap = new HashMap<>();
        expectedAttributeValueMap.put("nodeId", new ExpectedAttributeValue(new AttributeValue().withS(nodeId)));
        DynamoDBDeleteExpression deleteExpression = new DynamoDBDeleteExpression().withExpected(expectedAttributeValueMap);
        Node user = Node.builder()
                .nodeId(nodeId)
                .build();

        //First delete all the children nodes.
        deleteChildrenNodes(nodeId);
        mapper.delete(user, deleteExpression);
    }

    private void deleteChildrenNodes(String nodeId) {
        List<String> nodeIds = getChildNodeIds(nodeId, 0 , true);
        List<Node> nodesToDelete = new ArrayList<Node>();

        for (String id: nodeIds) {
            nodesToDelete.add(Node.builder().nodeId(id).build());
        }

        mapper.batchDelete(nodesToDelete);
    }
}
