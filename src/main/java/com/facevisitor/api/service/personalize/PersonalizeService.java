package com.facevisitor.api.service.personalize;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.personalizeevents.AmazonPersonalizeEvents;
import com.amazonaws.services.personalizeevents.AmazonPersonalizeEventsClientBuilder;
import com.amazonaws.services.personalizeevents.model.Event;
import com.amazonaws.services.personalizeevents.model.PutEventsRequest;
import com.amazonaws.services.personalizeevents.model.PutEventsResult;
import com.amazonaws.services.personalizeruntime.AmazonPersonalizeRuntime;
import com.amazonaws.services.personalizeruntime.AmazonPersonalizeRuntimeClientBuilder;
import com.amazonaws.services.personalizeruntime.model.GetPersonalizedRankingRequest;
import com.amazonaws.services.personalizeruntime.model.GetPersonalizedRankingResult;
import com.amazonaws.services.personalizeruntime.model.GetRecommendationsRequest;
import com.amazonaws.services.personalizeruntime.model.GetRecommendationsResult;
import com.facevisitor.api.dto.goods.GoodsDTO;
import com.facevisitor.api.repository.GoodsRepository;
import com.facevisitor.api.service.aws.AWSCredentialsService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
@Slf4j
public class PersonalizeService {


    final
    GoodsRepository goodsRepository;
    final
    ModelMapper modelMapper;
    final AWSCredentialsService awsCredentialsService;

    final
    Gson gson;
    String trackingId = "872bda53-fe70-4761-8704-69d357e47efa";
    @Autowired
    ObjectMapper objectMapper;


    public PersonalizeService(GoodsRepository goodsRepository, ModelMapper modelMapper, AWSCredentialsService awsCredentialsService, Gson gson) {
        this.goodsRepository = goodsRepository;
        this.modelMapper = modelMapper;
        this.awsCredentialsService = awsCredentialsService;
        this.gson = gson;
    }


    private AmazonPersonalizeRuntime runTimeClient() {
        return AmazonPersonalizeRuntimeClientBuilder.standard().withCredentials(awsCredentialsService.credentials()).withRegion(Regions.AP_NORTHEAST_2).build();
    }

    private AmazonPersonalizeEvents eventsClient() {
        return AmazonPersonalizeEventsClientBuilder.standard().withCredentials(awsCredentialsService.credentials()).withRegion(Regions.AP_NORTHEAST_2).build();
    }

    public List<GoodsDTO.GoodsListResponse> getRecommendations(Long userId) {
        GetRecommendationsRequest getRecommendationsRequest = new GetRecommendationsRequest();
        getRecommendationsRequest.withUserId(userId.toString()).withCampaignArn("arn:aws:personalize:ap-northeast-2:007731194585:campaign/MyFaceVisitorCampaign");
        GetRecommendationsResult recommendations = runTimeClient().getRecommendations(getRecommendationsRequest);
        List<Long> itemIds = recommendations.getItemList().stream().map(predictedItem -> Long.valueOf(predictedItem.getItemId())).collect(Collectors.toList());

        List<GoodsDTO.GoodsListResponse> goods = goodsRepository.getAll(itemIds).stream().map(goods1 -> modelMapper.map(goods1, GoodsDTO.GoodsListResponse.class)).collect(Collectors.toList());
        return goods;
    }

    public void viewEvent(Long userId, Long goodsId) throws JsonProcessingException {
        PutEventsRequest request = new PutEventsRequest();
        request.setUserId(userId.toString());
        request.setSessionId("session1");
        request.setTrackingId(trackingId);
        Event event = new Event();
        event.setEventId("ViewEvent");
        event.setEventType("VIEW");
        Map<String, Object> properties = new HashMap<>();
        properties.put("itemId", goodsId.toString());
        properties.put("eventValue", 2);
        event.setProperties(objectMapper.writeValueAsString(properties));
        event.setSentAt(new Date());
        request.setEventList(Collections.singletonList(event));
        PutEventsResult putEventsResult = eventsClient().putEvents(request);
        System.out.println(putEventsResult.getSdkResponseMetadata().toString());

    }

    public void likeEvent(Long userId, Long goodsId) throws JsonProcessingException {
        PutEventsRequest request = new PutEventsRequest();
        request.setUserId(userId.toString());
        request.setSessionId("session1");
        request.setTrackingId(trackingId);
        Event event = new Event();
        event.setEventId("ViewEvent");
        event.setEventType("VIEW");
        Map<String, Object> properties = new HashMap<>();
        properties.put("itemId", goodsId.toString());
        properties.put("eventValue", 2);
        event.setProperties(objectMapper.writeValueAsString(properties));
        event.setSentAt(new Date());
        request.setEventList(Collections.singletonList(event));
        PutEventsResult putEventsResult = eventsClient().putEvents(request);
        System.out.println(putEventsResult.getSdkResponseMetadata().toString());
    }

    public void disLikeEvent(Long userId, Long goodsId) throws JsonProcessingException {
        PutEventsRequest request = new PutEventsRequest();
        request.setUserId(userId.toString());
        request.setSessionId("session1");
        request.setTrackingId(trackingId);
        Event event = new Event();
        event.setEventId("ViewEvent");
        event.setEventType("VIEW");
        Map<String, Object> properties = new HashMap<>();
        properties.put("itemId", goodsId.toString());
        properties.put("eventValue", 2);
        event.setProperties(objectMapper.writeValueAsString(properties));
        event.setSentAt(new Date());
        request.setEventList(Collections.singletonList(event));
        PutEventsResult putEventsResult = eventsClient().putEvents(request);
        System.out.println(putEventsResult.getSdkResponseMetadata().toString());
    }

    public void cartEvent(Long userId, Long goodsId) throws JsonProcessingException {
        PutEventsRequest request = new PutEventsRequest();
        request.setUserId(userId.toString());
        request.setSessionId("session1");
        request.setTrackingId(trackingId);
        Event event = new Event();
        event.setEventId("ViewEvent");
        event.setEventType("VIEW");
        Map<String, Object> properties = new HashMap<>();
        properties.put("itemId", goodsId.toString());
        properties.put("eventValue", 2);
        event.setProperties(objectMapper.writeValueAsString(properties));
        event.setSentAt(new Date());
        request.setEventList(Collections.singletonList(event));
        PutEventsResult putEventsResult = eventsClient().putEvents(request);
        System.out.println(putEventsResult.getSdkResponseMetadata().toString());
    }

    public void orderEvent(Long userId, Long goodsId) throws JsonProcessingException {
        PutEventsRequest request = new PutEventsRequest();
        request.setUserId(userId.toString());
        request.setSessionId("session1");
        request.setTrackingId(trackingId);
        Event event = new Event();
        event.setEventId("ViewEvent");
        event.setEventType("VIEW");
        Map<String, Object> properties = new HashMap<>();
        properties.put("itemId", goodsId.toString());
        properties.put("eventValue", 2);
        event.setProperties(objectMapper.writeValueAsString(properties));
        event.setSentAt(new Date());
        request.setEventList(Collections.singletonList(event));
        PutEventsResult putEventsResult = eventsClient().putEvents(request);
        System.out.println(putEventsResult.getSdkResponseMetadata().toString());
    }

    public List<GoodsDTO.GoodsListResponse> getRankings(Long userId) {
        GetPersonalizedRankingRequest request = new GetPersonalizedRankingRequest();
        request.withUserId(userId.toString()).withCampaignArn("arn:aws:personalize:ap-northeast-2:007731194585:campaign/MyFaceVisitorCampaign");
        GetPersonalizedRankingResult personalizedRanking = runTimeClient().getPersonalizedRanking(request);
        List<Long> itemIds = personalizedRanking.getPersonalizedRanking().stream().map(predictedItem -> Long.valueOf(predictedItem.getItemId())).collect(Collectors.toList());
        List<GoodsDTO.GoodsListResponse> goods = goodsRepository.getAll(itemIds).stream().map(goods1 -> modelMapper.map(goods1, GoodsDTO.GoodsListResponse.class)).collect(Collectors.toList());
        return goods;
    }
}
