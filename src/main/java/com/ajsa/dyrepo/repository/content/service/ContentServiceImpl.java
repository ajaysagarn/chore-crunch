package com.ajsa.dyrepo.repository.content.service;

import com.ajsa.dyrepo.repository.content.dao.ContentDao;
import com.ajsa.dyrepo.repository.content.model.Content;
import com.ajsa.dyrepo.repository.content.model.ReadContentResponse;
import com.ajsa.dyrepo.repository.node.model.Node;
import com.ajsa.dyrepo.repository.node.service.NodeCrudService;
import com.ajsa.dyrepo.util.RepositoryException;
import com.ajsa.dyrepo.util.RepositoryUtils;
import com.amazonaws.services.s3.model.PutObjectResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ContentServiceImpl implements ContentService{

    @Autowired
    private ContentDao contentDao;
    @Autowired
    private NodeCrudService nodeService;


    @Override
    public Content addContent(String nodeIdPath, String contentId, MultipartFile file) throws RepositoryException, IOException {

        Node node = nodeService.readNode(nodeIdPath,0).get();

        Content content = Content.builder().id((contentId == null || contentId.isEmpty())?"content":contentId)
                .fileName(file.getOriginalFilename()).length(file.getSize()).path(
                        constructContentPath(node.getPath(), contentId)
                ).build();

        PutObjectResult res = contentDao.addContent(content.getPath(),file);
        content.setETag(res.getETag());

        List<Content> nodeContent = node.getContent();
        if(nodeContent == null)
            nodeContent = new ArrayList<>();

        Optional<Content> ccon = getNodeContentWithId(node, contentId);
        if(ccon.isPresent()){
            nodeContent.remove(ccon.get());
        }
        nodeContent.add(content);
        node.setContent(nodeContent);
        nodeService.updateNode(node);

        return content;
    }

    @Override
    public ReadContentResponse readContent(String nodeIdPath, String contentId) throws RepositoryException, IOException {
        Node node = nodeService.readNode(nodeIdPath,0).get();
        Optional<Content> ccon = getNodeContentWithId(node, contentId);

        if(ccon.isEmpty()){
            throw new RepositoryException(HttpStatus.NOT_FOUND.value(), "content with id "+contentId+"does not exist.");
        }

        ReadContentResponse contentResponse = new ReadContentResponse();

        contentResponse.setContent(contentDao.readContent(ccon.get().getPath(),ccon.get().getFileName()));
        contentResponse.setFileName(ccon.get().getFileName());

        return contentResponse;
    }

    @Override
    public void deleteContent(String nodeIdPath, String contentId) throws RepositoryException {
        Node node = nodeService.readNode(nodeIdPath,0).get();
        List nodeContent = node.getContent();
        Optional<Content> ccon = getNodeContentWithId(node, contentId);

        if(ccon.isEmpty()){
            throw new RepositoryException(HttpStatus.NOT_FOUND.value(), "content with id "+contentId+"does not exist.");
        }else{
            nodeContent.remove(ccon.get());
        }

        node.setContent(nodeContent);
        nodeService.updateNode(node);
        contentDao.deleteContent(ccon.get().getPath());

    }

    @Override
    public void deleteAllContent(String nodeIdPath) throws RepositoryException {
        Node node = nodeService.readNode(nodeIdPath,0).get();
        List<Content> nodeContent = node.getContent();
        if(nodeContent != null && !nodeContent.isEmpty()){
            for (Content c: nodeContent) {
                contentDao.deleteContent(c.getPath());
            }
        }
    }

    @Override
    public void deleteNodeFolder(String nodePath) throws RepositoryException {

        if(RepositoryUtils.isNodePathNodeId(nodePath)){
            Node node = nodeService.readNode(nodePath,0).get();
            nodePath = node.getPath();
        }
        contentDao.deleteFolder(nodePath.concat("/"));
    }


    private String constructContentPath(String nodePath, String contentId){
        if(contentId == null || contentId.isEmpty() || contentId == "content"){
            return nodePath.concat("/content");
        }
        if(contentId.indexOf('-') < 0){
            return nodePath.concat("/").concat(contentId);
        }
        return nodePath.concat("/")
                .concat(contentId.substring(0,contentId.indexOf('-')))
                .concat("/").concat(contentId.substring(contentId.indexOf('-') + 1,contentId.length()));
    }


    private Optional<Content> getNodeContentWithId(Node node, String contentId){
        List<Content> nodeContent = node.getContent();

        if(nodeContent == null)
            nodeContent = new ArrayList<Content>();

        return nodeContent.stream().filter(c -> c.getId().equals((contentId == null || contentId.isEmpty())?"content":contentId)).findFirst();
    }
}
