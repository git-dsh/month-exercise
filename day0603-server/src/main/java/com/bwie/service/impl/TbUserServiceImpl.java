package com.bwie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bwie.mapper.TbUserMapper;
import com.bwie.pojo.TbUser;
import com.bwie.service.TbUserService;
import com.bwie.util.R;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
* @author dsh
* @description 针对表【tb_user】的数据库操作Service实现
* @createDate 2024-06-03 20:07:20
*/
@Service
public class TbUserServiceImpl extends ServiceImpl<TbUserMapper, TbUser>
    implements TbUserService{
    @Autowired
    TbUserMapper tbUserMapper;

    @Override
    public R login(TbUser tbUser) {
        TbUser tbUser1 = tbUserMapper.selectOne(new QueryWrapper<TbUser>().lambda().eq(TbUser::getUserName, tbUser.getUserName()));
        if(tbUser1==null){
            return R.error(404,"用户不存在");
        }
        if(!tbUser1.getPassword().equals(tbUser.getPassword())){
            return R.error(500,"密码错误");
        }
        String token = Jwts.builder().signWith(SignatureAlgorithm.HS256, "bwie")
                .claim("id", tbUser1.getId())
                .claim("userName", tbUser1.getUserName())
                .claim("userRole", tbUser1.getUserRole())
                .compact();
        tbUser1.setToken(token);
        return R.success(tbUser1);
    }
}




