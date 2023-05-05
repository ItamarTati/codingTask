package com.daofab.restapi;

import com.daofab.restapi.dto.Child;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.daofab.restapi.dto.Parent;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@CrossOrigin(origins = {"http://localhost:4200", "https://coding-task-front-end.vercel.app/"})
public class Controller {
    private final ObjectMapper mapper = new ObjectMapper();


    @GetMapping("/parents")
    public List<Parent> getParentTransactions(@RequestParam(defaultValue = "1") int page) throws IOException {
        int pageSize = 2;
        int offset = (page - 1) * pageSize;
        Resource parentResource = new ClassPathResource("Parent.json");
        InputStream inputStream = parentResource.getInputStream();
        JsonNode ParentRootNode = mapper.readTree(inputStream);
        JsonNode ParentDataNode = ParentRootNode.get("data");
        CollectionType parentListType = mapper.getTypeFactory().constructCollectionType(List.class, Parent.class);
        List<Parent> parents = mapper.readValue(ParentDataNode.traverse(), parentListType);

        if (offset >= parents.size()) {
            return Collections.emptyList();
        }

        int endIndex = Math.min(offset + pageSize, parents.size());
        return parents.subList(offset, endIndex);
    }

    @GetMapping("/children/{parentId}")
    public List<Child> getChildTransactions(@PathVariable int parentId) throws IOException {
        Resource childResource = new ClassPathResource("Child.json");
        InputStream inputStream = childResource.getInputStream();
        JsonNode ChildRootNode = mapper.readTree(inputStream);
        JsonNode ChildDataNode = ChildRootNode.get("data");
        CollectionType childListType = mapper.getTypeFactory().constructCollectionType(List.class, Child.class);
        List<Child> children = mapper.readValue(ChildDataNode.traverse(), childListType);

        return children.stream()
                .filter(child -> child.getParentId() == parentId)
                .collect(Collectors.toList());
    }
}