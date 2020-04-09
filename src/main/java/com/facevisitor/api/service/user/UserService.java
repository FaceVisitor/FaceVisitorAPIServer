package com.facevisitor.api.service.user;

import com.facevisitor.api.common.exception.NotFoundException;
import com.facevisitor.api.common.exception.NotFoundGoodsException;
import com.facevisitor.api.common.exception.NotFoundUserException;
import com.facevisitor.api.domain.base.BaseEntity;
import com.facevisitor.api.domain.goods.Goods;
import com.facevisitor.api.domain.user.User;
import com.facevisitor.api.repository.GoodsRepository;
import com.facevisitor.api.repository.UserRepository;
import com.facevisitor.api.service.personalize.PersonalizeService;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
@Transactional
public class UserService {

    final UserRepository userRepository;

    final
    GoodsRepository goodsRepository;

    final ModelMapper modelMapper;

    @Autowired
    PersonalizeService personalizeService;

    public UserService(UserRepository userRepository, ModelMapper modelMapper, GoodsRepository goodsRepository) {
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
        this.goodsRepository = goodsRepository;
    }


    @Transactional(readOnly = true)
    public Boolean exist(String email) {
        return userRepository.findByEmail(email).isPresent();
    }


    @Transactional(readOnly = true)
    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(NotFoundException::new);
    }

    @Transactional(readOnly = true)
    public User getUserByFaceIds(List<String> faceId) {
        return userRepository.findByFaceIds(faceId).orElseThrow(NotFoundException::new);
    }

    @Transactional(readOnly = true)
    public User getUserById(Long id) {
        return userRepository.findById(id).orElseThrow(NotFoundException::new);
    }


    public User updateUser(User updated) {
        User ori = userRepository.findById(updated.getId()).orElseThrow(NotFoundException::new);
        modelMapper.map(updated, ori);
        return ori;
    }

    public void deleteUser(Long id) {
        User user = userRepository.findById(id).orElseThrow(NotFoundException::new);
        user.setAuthorities(null);
        userRepository.deleteById(id);

    }

    public boolean likeGoods(String email, Long goods_id) {
        User user = userRepository.findByEmail(email).orElseThrow(NotFoundUserException::new);
        Set<Goods> existGoods = user.getGoodsLike().stream()
                .filter(likeGoods -> likeGoods.getId().equals(goods_id)).collect(Collectors.toSet());
        //좋아요 해제
        if (existGoods.size() > 0) {
            user.removeGoodsLike(goods_id);
            return false;
        } else {
            //좋아요
            Goods goods = goodsRepository.findById(goods_id)
                    .orElseThrow(() -> new NotFoundGoodsException(goods_id));
            user.addGoodsLike(goods);
            return true;
        }
    }

    @Transactional(readOnly = true)
    public Set<Goods> listLikeGoods(String email) {
        User user = userRepository.findByEmail(email).orElseThrow(NotFoundUserException::new);
        return user.getGoodsLike()
                .stream().sorted(Comparator.comparing(BaseEntity::getCreatedAt)
                        .reversed()).collect(Collectors.toCollection(LinkedHashSet::new));
    }

    public void viewGoodsEvent(String email, Long goodId) throws JsonProcessingException {
        User user = userRepository.findByEmail(email).orElseThrow(NotFoundUserException::new);
        Long userId = user.getId();
        personalizeService.viewEvent(userId, goodId);
    }


}
