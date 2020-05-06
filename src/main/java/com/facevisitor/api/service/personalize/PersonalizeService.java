package com.facevisitor.api.service.personalize;

import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.personalizeevents.AmazonPersonalizeEvents;
import com.amazonaws.services.personalizeevents.AmazonPersonalizeEventsClientBuilder;
import com.amazonaws.services.personalizeevents.model.Event;
import com.amazonaws.services.personalizeevents.model.PutEventsRequest;
import com.amazonaws.services.personalizeevents.model.PutEventsResult;
import com.amazonaws.services.personalizeruntime.AmazonPersonalizeRuntime;
import com.amazonaws.services.personalizeruntime.AmazonPersonalizeRuntimeClientBuilder;
import com.amazonaws.services.personalizeruntime.model.GetRecommendationsRequest;
import com.amazonaws.services.personalizeruntime.model.GetRecommendationsResult;
import com.facevisitor.api.dto.goods.GoodsDTO;
import com.facevisitor.api.repository.GoodsRepository;
import com.facevisitor.api.repository.UserRepository;
import com.facevisitor.api.service.aws.AWSCredentialsService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
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
    UserRepository userRepository;

    final
    ModelMapper modelMapper;
    final AWSCredentialsService awsCredentialsService;

    final
    Gson gson;
    String trackingId = "02182ca8-6611-4987-a2eb-6fc27cab63cf";
    final
    ObjectMapper objectMapper;
    String metaArn = "arn:aws:personalize:ap-northeast-2:901620516009:campaign/ML";
    String popularityArn = "arn:aws:personalize:ap-northeast-2:901620516009:campaign/Popul";


    public PersonalizeService(GoodsRepository goodsRepository, ModelMapper modelMapper, AWSCredentialsService awsCredentialsService, Gson gson, ObjectMapper objectMapper, UserRepository userRepository) {
        this.goodsRepository = goodsRepository;
        this.modelMapper = modelMapper;
        this.awsCredentialsService = awsCredentialsService;
        this.gson = gson;
        this.objectMapper = objectMapper;
        this.userRepository = userRepository;
    }


    private AmazonPersonalizeRuntime runTimeClient() {
        return AmazonPersonalizeRuntimeClientBuilder.standard().withCredentials(awsCredentialsService.credentials()).withRegion(Regions.AP_NORTHEAST_2).build();
    }

    private AmazonPersonalizeEvents eventsClient() {

        AwsClientBuilder.EndpointConfiguration endpointConfiguration = new AwsClientBuilder.EndpointConfiguration("personalize-events.ap-northeast-2.amazonaws.com", "ap-northeast-2");
        AmazonPersonalizeEventsClientBuilder amazonPersonalizeEventsClientBuilder = AmazonPersonalizeEventsClientBuilder.standard().withCredentials(awsCredentialsService.credentials());
        amazonPersonalizeEventsClientBuilder.setEndpointConfiguration(endpointConfiguration);
        return amazonPersonalizeEventsClientBuilder.build();
    }

    public List<GoodsDTO.GoodsListResponse> getRecommendations(Long userId) {
        GetRecommendationsRequest getRecommendationsRequest = new GetRecommendationsRequest();
        getRecommendationsRequest.withUserId(userId.toString())
                .withCampaignArn(metaArn);
        GetRecommendationsResult recommendations = runTimeClient().getRecommendations(getRecommendationsRequest);
        List<Long> itemIds = recommendations.getItemList().
                stream().map(predictedItem -> Long.valueOf(predictedItem.getItemId())).collect(Collectors.toList());

        return goodsRepository.
                getAll(itemIds).stream().map(goods1 -> modelMapper.
                map(goods1, GoodsDTO.GoodsListResponse.class)).collect(Collectors.toList());
    }

    public List<GoodsDTO.GoodsListResponse> getPopularity(Long userId) {
        GetRecommendationsRequest getRecommendationsRequest = new GetRecommendationsRequest();
        getRecommendationsRequest.withUserId(userId.toString())
                .withCampaignArn(popularityArn);
        GetRecommendationsResult recommendations = runTimeClient().getRecommendations(getRecommendationsRequest);
        List<Long> itemIds = recommendations.getItemList().
                stream().map(predictedItem -> Long.valueOf(predictedItem.getItemId())).collect(Collectors.toList());

        return goodsRepository.
                getAll(itemIds).stream().map(goods1 -> modelMapper.
                map(goods1, GoodsDTO.GoodsListResponse.class)).collect(Collectors.toList());
    }

    public void viewEvent(Long userId, Long goodsId) throws JsonProcessingException {
        PutEventsRequest request = createEventRequest(userId);
        request.setEventList(createEvent(goodsId, EventType.VIEW));
        PutEventsResult putEventsResult = eventsClient().putEvents(request);
        System.out.println(putEventsResult.getSdkResponseMetadata().toString());
    }

    public void likeEvent(Long userId, Long goodsId) throws JsonProcessingException {
        PutEventsRequest request = createEventRequest(userId);
        request.setEventList(createEvent(goodsId, EventType.LIKE));
        PutEventsResult putEventsResult = eventsClient().putEvents(request);
        System.out.println(putEventsResult.getSdkResponseMetadata().toString());
    }

    public void disLikeEvent(Long userId, Long goodsId) throws JsonProcessingException {
        PutEventsRequest request = createEventRequest(userId);
        request.setEventList(createEvent(goodsId, EventType.DISLIKE));
        PutEventsResult putEventsResult = eventsClient().putEvents(request);
        System.out.println(putEventsResult.getSdkResponseMetadata().toString());
    }

    public void cartEvent(Long userId, Long goodsId) throws JsonProcessingException {
        PutEventsRequest request = createEventRequest(userId);
        request.setEventList(createEvent(goodsId, EventType.CART));
        PutEventsResult putEventsResult = eventsClient().putEvents(request);
        System.out.println(putEventsResult.getSdkResponseMetadata().toString());
    }

    public void orderEvent(Long userId, Long goodsId) throws JsonProcessingException {
        PutEventsRequest request = createEventRequest(userId);
        request.setEventList(createEvent(goodsId, EventType.ORDER));
        PutEventsResult putEventsResult = eventsClient().putEvents(request);
        System.out.println(putEventsResult.getSdkResponseMetadata().toString());
    }

    public PutEventsRequest createEventRequest(Long userId) {
        PutEventsRequest request = new PutEventsRequest();
        request.setUserId(userId.toString());
        request.setSessionId("session1");
        request.setTrackingId(trackingId);
        return request;
    }

    public Collection<Event> createEvent(Long itemId, EventType eventType) throws JsonProcessingException {
        Event event = new Event();
        event.setEventId(event.getEventId());
        event.setEventType(eventType.name());
        Map<String, Object> properties = new HashMap<>();
        properties.put("itemId", itemId.toString());
        properties.put("eventValue", eventType.getEventValue());
        event.setProperties(objectMapper.writeValueAsString(properties));
        event.setSentAt(new Date());
        return Collections.singleton(event);
    }

    @Getter
    public enum EventType {
        VIEW(2, "ViewEvent"),
        LIKE(4, "LikeEvent"),
        DISLIKE(-4, "DislikeEvent"),
        CART(8, "CartEvent"),
        ORDER(10, "OrderEvent"),
        FACE(10, "FaceEvent");

        private int eventValue;
        private String eventId;

        EventType(int eventValue, String eventId) {

            this.eventValue = eventValue;
            this.eventId = eventId;
        }

    }
}
