package com.juity.blog.SERVICE.meta.impl;

import com.juity.blog.CONSTANT.ErrorConstant;
import com.juity.blog.CONSTANT.Types;
import com.juity.blog.CONSTANT.webConst;
import com.juity.blog.DAO.metaDao;
import com.juity.blog.DTO.cond.metaCond;
import com.juity.blog.DTO.metaDto;
import com.juity.blog.EXCEPTION.BusinessException;
import com.juity.blog.POJO.content;
import com.juity.blog.POJO.meta;
import com.juity.blog.SERVICE.content.contentService;
import com.juity.blog.SERVICE.meta.metaService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class metaServiceImpl implements metaService {
    @Autowired
    private metaDao metaDao;
    @Autowired
    private contentService contentService;

    @Override
//    @Cacheable(value = "metaCaches", key = "'metaList_' + #p0")
    public List<metaDto> getMetaList(String type, int limit) {
        if (StringUtils.isNotBlank(type)) {
            if (limit < 1 || limit > webConst.MAX_POSTS) {
                limit = 10;
            }
            Map<String, Object> paraMap = new HashMap<>();
            paraMap.put("type", type);
            paraMap.put("limit", limit);
            return metaDao.selectFromSql(paraMap);
        }
        return null;
    }

    @Override
    public void addMetas(Integer cid, String names, String type) {
        if (null == cid)
            throw BusinessException.withErrorCode(ErrorConstant.Common.PARAM_IS_EMPTY);
        if (StringUtils.isNotBlank(names) && StringUtils.isNotBlank(type)) {
            String[] nameArr = StringUtils.split(names, ",");
            for (String name : nameArr) {
                this.saveOrUpdate(name, type);
            }
        }
    }

    @Override
    public void MinusMetas(Integer cid) {
        content article = contentService.getArticleById(cid);
        if (article != null) {
            String tags = article.getTags();
            if (StringUtils.isNotBlank(tags)) {
                String[] nameArr = StringUtils.split(tags, ",");
                for (String name : nameArr) {
                    metaDao.minusCount(name, Types.TAG.getType());
                }
            }

            String categories = article.getCategories();
            if (StringUtils.isNotBlank(categories)) {
                String[] nameArr = StringUtils.split(categories, ",");
                for (String name : nameArr) {
                    metaDao.minusCount(name, Types.CATEGORY.getType());
                }
            }
        }
    }

    private void saveOrUpdate(String name, String type) {
        metaCond metaCond = new metaCond();
        metaCond.setName(name);
        metaCond.setType(type);
        List<meta> metas = this.getMetas(metaCond);

        // 创建对应meta
        int mid;
        if (metas.size() == 1) {
            mid = metas.get(0).getMid();
        } else if (metas.size() > 1) {
            throw BusinessException.withErrorCode(ErrorConstant.Meta.NOT_ONE_RESULT);
        } else {
            meta metaDomain = new meta();
            metaDomain.setSlug(name);
            metaDomain.setName(name);
            metaDomain.setType(type);
            this.addMeta(metaDomain);
            mid = metaDomain.getMid();
        }

        // 增加文章数
        metaDao.addCount(mid);
    }

    @Override
    public void addMeta(meta meta) {
        if (null == meta)
            throw BusinessException.withErrorCode(ErrorConstant.Common.PARAM_IS_EMPTY);
        metaDao.addMeta(meta);
    }

    @Override
    public void delMetaById(Integer mid) {
        if (null == mid)
            throw BusinessException.withErrorCode(ErrorConstant.Common.PARAM_IS_EMPTY);
        metaDao.delMetaById(mid);
    }

    @Override
    public void saveMeta(String type, String name, Integer mid) {
        if (StringUtils.isNotBlank(type) && StringUtils.isNotBlank(name)) {
            metaCond metaCond = new metaCond();
            metaCond.setName(name);
            metaCond.setType(type);
            List<meta> metas = metaDao.getMetaByCond(metaCond);
            if (null == metas || metas.size() == 0) {
                meta metaDomain = new meta();
                metaDomain.setName(name);
                if (null != mid) {
                    meta meta = metaDao.getMetaById(mid);
                    if (null != meta) {
                        metaDomain.setMid(mid);
                        metaDao.updateMeta(metaDomain);
                        contentService.updateCategory(meta.getName(), name);
                    }
                } else {
                    metaDomain.setType(type);
                    metaDao.addMeta(metaDomain);
                }
            } else {
                throw BusinessException.withErrorCode(ErrorConstant.Meta.META_IS_EXIST);
            }
        }
    }

    public List<meta> getMetas(metaCond metaCond) {
        return metaDao.getMetaByCond(metaCond);
    }

    @Override
    public void updateMeta(meta meta) {
        if (null == meta || null == meta.getMid())
            throw BusinessException.withErrorCode(ErrorConstant.Common.PARAM_IS_EMPTY);
        metaDao.updateMeta(meta);
    }
}
