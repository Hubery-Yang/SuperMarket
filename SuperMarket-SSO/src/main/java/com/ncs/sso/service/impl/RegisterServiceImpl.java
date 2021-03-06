package com.ncs.sso.service.impl;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ncs.common.utils.pojo.SmResult;
import com.ncs.mapper.TbUserMapper;
import com.ncs.pojo.TbUser;
import com.ncs.pojo.TbUserExample;
import com.ncs.pojo.TbUserExample.Criteria;
import com.ncs.sso.service.RegisterService;

@Service
public class RegisterServiceImpl implements RegisterService {

	@Autowired
	public TbUserMapper tbUserMapper;

	// @param type 1: phone 2:email 3:username
	@Override
	public SmResult CheckInfo(String param, int type) throws Exception {

		TbUserExample example = new TbUserExample();
		Criteria criteria = example.createCriteria();

		// to check the type
		if (type == 1) {
			criteria.andPhoneEqualTo(param);
		} else if (type == 2) {
			criteria.andEmailEqualTo(param);
		} else {
			criteria.andUsernameEqualTo(param);
		}

		List<TbUser> result = tbUserMapper.selectByExample(example);

		if (result == null || result.isEmpty()) {
			return SmResult.ok(true);
		}

		return SmResult.ok(false);
	}

	@Override
	public SmResult Registe(TbUser user) throws Exception {
		
		if(StringUtils.isBlank(user.getUsername())||StringUtils.isBlank(user.getPassword())) {
			SmResult.build(400, "username or the password can't be empty");
		}
		
		//check the user's info
		if(StringUtils.isNotBlank(user.getPhone())){
			SmResult checkPhone = CheckInfo(user.getPhone(), 1);
			if(!(Boolean)checkPhone.getData()) {
				SmResult.build(400, "user's phone repeat");
			}
		}
		
		if(StringUtils.isNotBlank(user.getEmail())){
		SmResult checkEmail = CheckInfo(user.getEmail(), 2);
		if(!(Boolean)checkEmail.getData()) {
			SmResult.build(400, "user's email repeat");
		}
		}
		
		user.setCreated(new Date());
		user.setUpdated(new Date());
	 tbUserMapper.insert(user);
		
		return SmResult.ok();
	}

}
