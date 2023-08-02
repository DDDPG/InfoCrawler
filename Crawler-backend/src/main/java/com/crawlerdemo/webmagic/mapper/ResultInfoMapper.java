package com.crawlerdemo.webmagic.mapper;

import com.crawlerdemo.webmagic.Entity.ResultInfoEntity;
import com.github.yulichang.base.MPJBaseMapper;

import java.util.List;

//@CacheNamespace(implementation = org.mybatis.caches.redis.RedisCache.class)
//@Mapper
public interface ResultInfoMapper extends MPJBaseMapper<ResultInfoEntity> {

    int filterAndInsert(ResultInfoEntity resultInfoEntity);

    List<ResultInfoEntity> selectRecentChanged();
}
