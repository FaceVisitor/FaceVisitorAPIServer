package com.facevisitor.api.service.goods;

import com.facevisitor.api.domain.goods.Goods;
import com.facevisitor.api.domain.goods.GoodsHistory;
import com.facevisitor.api.domain.user.User;
import com.facevisitor.api.dto.goods.GoodsDTO;
import com.facevisitor.api.repository.GoodsHistoryRepository;
import com.facevisitor.api.service.user.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
@Slf4j
@AllArgsConstructor
public class GoodsHistoryService {
    GoodsHistoryRepository goodsHistoryRepository;
    UserService userService;
    GoodsUserService goodsUserService;
    ModelMapper modelMapper;

    public void addGoodsHistory(User user, Goods goods) {
        Optional<GoodsHistory> firstByUserAndGoods = goodsHistoryRepository.findFirstByUserAndGoods(user, goods);
        if (firstByUserAndGoods.isPresent()) {
            GoodsHistory existGoodsHistory = firstByUserAndGoods.get();
            existGoodsHistory.setUpdatedAt(LocalDateTime.now());
        } else {
            GoodsHistory goodsHistory = new GoodsHistory(user, goods);
            goodsHistoryRepository.save(goodsHistory);
        }

    }

    public List<GoodsDTO.GoodsListResponse> getHistory(String userEmail) {
        User userByEmail = userService.getUserByEmail(userEmail);
        List<GoodsHistory> goodsHistories = goodsHistoryRepository.findAllByUser(userByEmail);
        return goodsHistories.stream().map(goodsHistory -> {
            Goods goods = goodsHistory.getGoods();
            return modelMapper.map(goods, GoodsDTO.GoodsListResponse.class);
        }).collect(Collectors.toList());
    }

}
