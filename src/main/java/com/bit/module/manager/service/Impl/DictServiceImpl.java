package com.bit.module.manager.service.Impl;

import com.bit.base.service.BaseService;
import com.bit.base.vo.BaseVo;
import com.bit.module.manager.bean.Dict;
import com.bit.module.manager.dao.DictDao;
import com.bit.module.manager.service.DictService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Dict的Service实现类
 * @author zhangjie
 * @date 2018-12-28
 */
@Service("dictService")
public class DictServiceImpl extends BaseService implements DictService {

    private static final Logger logger = LoggerFactory.getLogger(DictServiceImpl.class);

    @Autowired
    private DictDao dictDao;


    /**
     * 通过主键查询单个Dict
     * @param id
     * @return Dict
     */
    @Override
    public Dict findById(Long id) {
        return dictDao.findById(id);
    }

    /**
     * 保存Dict
     * @param dict
     */
    @Override
    @Transactional
    public void add(Dict dict) {
        dictDao.add(dict);
    }

    /**
     * 更新Dict
     * @param dict
     */
    @Override
    @Transactional
    public void update(Dict dict) {
        dictDao.update(dict);
    }

    /**
     * 删除Dict
     * @param id
     */
    @Override
    @Transactional
    public void delete(Long id) {
        dictDao.delete(id);
    }

    /**
     * 根据module 查询字典
     * @param module
     * @return
     */
    @Override
    public List<Dict> findByModule(String module) {
        return dictDao.findByModule(module);
    }

    @Override
    public BaseVo findByModuleAndCode(Dict dict) {
        Dict byModuleAndDictCode = dictDao.findByModuleAndDictCode(dict);
        BaseVo baseVo = new BaseVo();
        baseVo.setData(byModuleAndDictCode);
        return baseVo;
    }


    /**
     * 根据模块名称集合批量查询字典表
     * @param dict
     * @return
     */
    @Override
    public BaseVo findByModules(Dict dict) {
        Map<String,List<Dict>> result = new HashMap<>();

        List<String> modules = dict.getModules();
        List<Dict> dicts = dictDao.findByModules(dict);
        for (int i = 0;i<modules.size();i++){
            String module = modules.get(i);
            List<Dict> dictList = new ArrayList<>();
            for (int j = 0;j < dicts.size();j++){
                if (module.equals(dicts.get(j).getModule())){
                    dictList.add(dicts.get(j));
                }
            }
            result.put(module,dictList);

        }
        BaseVo baseVo = new BaseVo();
        baseVo.setData(result);
        return baseVo;
    }

}
