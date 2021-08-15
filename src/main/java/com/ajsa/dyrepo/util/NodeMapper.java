package com.ajsa.dyrepo.util;

import com.ajsa.dyrepo.repository.node.model.Node;
import com.ajsa.dyrepo.repository.property.model.Property;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class NodeMapper {

    ObjectMapper mapper = new ObjectMapper();

    public <T> Node toNode(T source) {
        Map<String,Object> s = mapper.convertValue(source, new TypeReference<Map<String, Object>>() {});
        Node node = new Node();
        if(s.containsKey("id") && !s.get("id").equals(null)){
            node.setNodeId(s.get("id").toString());
        }
        if(s.containsKey("name") && !s.get("name").equals(null)){
            node.setName(s.get("name").toString());
        }

        List<Property> properties = new ArrayList<>();

        for(Map.Entry<String, Object> value: s.entrySet()){
            String type = value.getValue().getClass().getName();
            properties.add(Property.builder()
                    .name(value.getKey())
                    .type(type.substring(type.lastIndexOf('.')+1,type.length()))
                    .id(value.getKey()).value(value.getValue().toString()).build());
        }

        node.setProperties(properties);
        return node;
    }

    public <T> T nodeToClass(Node node, Class<T> destClass){
        Map<String,Object> d = new HashMap<>();
        List<Property> nodeProperties = node.getProperties();

        if(nodeProperties!= null){
            for (Property p: nodeProperties) {
                d.put(p.getName(),p.getValue());
            }
        }
        return mapper.convertValue(d,destClass);
    }

}
