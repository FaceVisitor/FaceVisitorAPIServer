package com.facevisitor.api.service.personalize;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.personalizeevents.AmazonPersonalizeEvents;
import com.amazonaws.services.personalizeevents.AmazonPersonalizeEventsClientBuilder;
import com.amazonaws.services.personalizeruntime.AmazonPersonalizeRuntime;
import com.amazonaws.services.personalizeruntime.AmazonPersonalizeRuntimeClientBuilder;
import com.amazonaws.services.personalizeruntime.model.GetPersonalizedRankingRequest;
import com.amazonaws.services.personalizeruntime.model.GetPersonalizedRankingResult;
import com.amazonaws.services.personalizeruntime.model.GetRecommendationsRequest;
import com.amazonaws.services.personalizeruntime.model.GetRecommendationsResult;
import com.facevisitor.api.dto.goods.GoodsDTO;
import com.facevisitor.api.repository.GoodsRepository;
import com.facevisitor.api.service.aws.AWSCredentialsService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
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

    public PersonalizeService(GoodsRepository goodsRepository, ModelMapper modelMapper, AWSCredentialsService awsCredentialsService) {
        this.goodsRepository = goodsRepository;
        this.modelMapper = modelMapper;
        this.awsCredentialsService = awsCredentialsService;
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

    public void viewEvent(Long userId, Long goodsId) {

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
