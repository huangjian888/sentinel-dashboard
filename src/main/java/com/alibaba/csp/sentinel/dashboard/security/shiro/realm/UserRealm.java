package com.alibaba.csp.sentinel.dashboard.security.shiro.realm;

import com.alibaba.csp.sentinel.dashboard.service.NacosDynamicAccountsServiceImpl;
import com.alibaba.csp.sentinel.dashboard.util.UserUtils;
import com.google.common.collect.Sets;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

import com.alibaba.csp.sentinel.dashboard.security.shiro.filter.authc.UsernamePasswordToken;
import org.springframework.beans.factory.annotation.Autowired;

/**
 */
public class UserRealm extends AuthorizingRealm {
	@Autowired
	private NacosDynamicAccountsServiceImpl nacosDynamicAccountsService;
	/**
	 * 授权的回调方法
	 */
	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
		SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
		Set<String> roles = Sets.newConcurrentHashSet();
		roles.add("admin");
		authorizationInfo.setRoles(roles);
		return authorizationInfo;
	}

	/**
	 * 认证的回调方法
	 */
	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
		UsernamePasswordToken authcToken = (UsernamePasswordToken) token;
		String username = authcToken.getUsername();
		//模拟从数据库或者从Nacos配置中心获取的用户信息
		User user = null;
		List<User> users = nacosDynamicAccountsService.getUsers();
		if(users != null){
			for(User _user : users){
				if(_user.getUsername().equals(username)){
					user = _user;
				}
			}
		}
		SimpleAuthenticationInfo authenticationInfo = new SimpleAuthenticationInfo(
				new Principal(user, authcToken.isMobileLogin()), // 用户名
				user.getPassword(), // 密码
				ByteSource.Util.bytes(user.getCredentialsSalt()), // salt=username+salt
				getName() // realm name
		);
		return authenticationInfo;
	}

	@Override
	public void clearCachedAuthorizationInfo(PrincipalCollection principals) {
		super.clearCachedAuthorizationInfo(principals);
	}

	@Override
	public void clearCachedAuthenticationInfo(PrincipalCollection principals) {
		super.clearCachedAuthenticationInfo(principals);
	}

	@Override
	public void clearCache(PrincipalCollection principals) {
		super.clearCache(principals);
	}

	public void clearAllCachedAuthorizationInfo() {
		getAuthorizationCache().clear();
	}

	public void clearAllCachedAuthenticationInfo() {
		getAuthenticationCache().clear();
	}

	public void clearAllCache() {
		clearAllCachedAuthenticationInfo();
		clearAllCachedAuthorizationInfo();
	}

	/**
	 * 授权用户信息
	 */
	public static class Principal implements Serializable {

		private static final long serialVersionUID = 1L;

		private String id; // 编号
		private String username; // 登录名
		private String realname; // 姓名
		private boolean mobileLogin; // 是否手机登录

		public Principal(User user, boolean mobileLogin) {
			this.id = user.getId();
			this.username = user.getUsername();
			this.realname = user.getRealname();
			this.mobileLogin = mobileLogin;
		}

		public String getId() {
			return id;
		}

		public String getUsername() {
			return username;
		}

		public String getRealname() {
			return realname;
		}

		public boolean isMobileLogin() {
			return mobileLogin;
		}

		/**
		 * 获取SESSIONID
		 */
		public String getSessionid() {
			try {
				return (String) UserUtils.getSession().getId();
			} catch (Exception e) {
				return "";
			}
		}

		@Override
		public String toString() {
			return id;
		}

	}
}
