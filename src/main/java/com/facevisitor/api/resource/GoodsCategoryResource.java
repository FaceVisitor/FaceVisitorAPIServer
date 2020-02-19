package com.facevisitor.api.resource;

import com.facevisitor.api.domain.goods.GoodsCategory;
import com.facevisitor.api.owner.controller.GoodsCategoryController;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

public class GoodsCategoryResource extends EntityModel<GoodsCategory> {
    public GoodsCategoryResource(GoodsCategory content, Link... links) {
        super(content, links);
        add(linkTo(GoodsCategoryController.class).slash(content.getId()).withSelfRel());
        add(linkTo(GoodsCategoryController.class).slash(content.getId()).withRel("update"));
        add(linkTo(GoodsCategoryController.class).slash(content.getId()).withRel("delete"));
    }
}
