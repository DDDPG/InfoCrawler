package com.crawlerdemo.webmagic.mapper.crawlermapper;

import com.crawlerdemo.webmagic.Entity.crawler.ResultInfoEntity;
import com.github.yulichang.base.MPJBaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

//@CacheNamespace(implementation = org.mybatis.caches.redis.RedisCache.class)
//@Mapper
public interface ResultInfoMapper extends MPJBaseMapper<ResultInfoEntity> {

    int filterAndInsert(ResultInfoEntity resultInfoEntity);

}
