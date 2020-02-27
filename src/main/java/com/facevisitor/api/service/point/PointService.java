package com.facevisitor.api.service.point;

import com.facevisitor.api.domain.order.FVOrder;
import com.facevisitor.api.domain.point.Point;
import com.facevisitor.api.repository.PointRepository;
import com.facevisitor.api.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@Transactional
public class PointService {

    @Autowired
    PointRepository pointRepository;

    @Autowired
    UserRepository userRepository;

    public Point savePoint(FVOrder fvOrder){
        Point point = Point.savePoint(fvOrder);
        return pointRepository.save(point);
    }

    public Point usePoint(FVOrder fvOrder, BigDecimal usePoint){
        Point point = Point.usePoint(fvOrder, usePoint);
        return pointRepository.save(point);
    }
}
