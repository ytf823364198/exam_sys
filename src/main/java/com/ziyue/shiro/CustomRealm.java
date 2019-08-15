package com.ziyue.shiro;

import java.util.List;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import com.ziyue.entity.BaseUser;
import com.ziyue.service.BaseMenuService;
import com.ziyue.service.BaseUserService;

import lombok.extern.slf4j.Slf4j;
@Component
@Slf4j
public class CustomRealm extends AuthorizingRealm{
    @Autowired
    @Lazy
    BaseUserService baseUserService;
    @Autowired
    @Lazy
    BaseMenuService baseMenuService;
    //认证（登录）
	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
		String username = (String) token.getPrincipal();
        String password = new String((char[]) token.getCredentials());
        log.debug("token.getCredentials()-->" + password );
        BaseUser user = baseUserService.findUserById(username);
        //账号不存在
        if(user == null) {
            throw new UnknownAccountException("账号或密码不正确");
        }
        //密码错误
        if(!password.equals(user.getPassword())) {
            throw new IncorrectCredentialsException("账号或密码不正确");
        }
        
        //账号锁定
        if("n".equals(user.getStatus()) ){
        	throw new LockedAccountException("账号已被禁用");
        }
        
        SimpleAuthenticationInfo info = new SimpleAuthenticationInfo(user, password, this.getName());
        log.debug("认证成功 SimpleAuthenticationInfo ");
        SecurityUtils.getSubject().getSession().setAttribute("loginUser", user);
        return info;
	}

	//授权
	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
		BaseUser user =  (BaseUser) principals.getPrimaryPrincipal();
		log.debug("获取授权中的用户 --->" + user);
		//用户权限列表
		List<String> permissions = baseMenuService.findMenuPermcodeByUserId(user.getId());
		
		log.debug("permissions 个数-->" + permissions.size());
		
		for(String s : permissions ) {
			log.debug("permissions -->" + s);
		}
		
		
		SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
		info.addStringPermissions(permissions);
		return info;
	}

	//清除缓存
	public void clearCached() {
		PrincipalCollection principals = SecurityUtils.getSubject().getPrincipals();
		super.clearCache(principals);
		log.debug("clearCached 清除缓存" );
	}

}
