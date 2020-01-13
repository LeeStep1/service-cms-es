package com.bit.module.manager.dao;

import com.bit.module.manager.bean.Dict;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Dict管理的Dao
 * @author zhangjie
 * @date 2018-12-28
 */
@Repository
public interface DictDao {


	/**
	 * 通过主键查询单个Dict
	 * @param id	 	 
	 * @return
	 */
	Dict findById(@Param(value = "id") Long id);

	/**
	 * 保存Dict
	 * @param dict
	 */
	void add(Dict dict);

	/**
	 * 更新Dict
	 * @param dict
	 */
	void update(Dict dict);

	/**
	 * 删除Dict
	 * @param id
	 */
	void delete(@Param(value = "id") Long id);

	/**
	 * 根据模块查询字典
	 * @param module
	 * @return
	 */
    List<Dict> findByModule(@Param(value = "module") String module);

	/**
	 * 根据module 和 dictcode 查询结果
	 * @param dict
	 * @return
	 */
	Dict findByModuleAndDictCode(Dict dict);


	/**
	 * 根据模块名称集合批量查询字典表
	 * @param dict
	 * @return
	 */
    List<Dict> findByModules(Dict dict);
}
