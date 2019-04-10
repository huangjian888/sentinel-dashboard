package com.alibaba.csp.sentinel.dashboard.util;

import com.alibaba.csp.sentinel.dashboard.security.shiro.realm.User;
import com.alibaba.csp.sentinel.dashboard.security.shiro.realm.UserRealm;
import net.sf.ehcache.Cache;
import net.sf.ehcache.Element;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.UnavailableSecurityManagerException;
import org.apache.shiro.session.InvalidSessionException;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;

import java.util.List;
import java.util.Set;

/**
 *
 * @description: 用户工具类
 *
 */
public class UserUtils {
	public static final String USER_CACHE = "userCache";
	public static final String USER_CACHE_ID_ = "id_";
	public static final String USER_CACHE_USER_NAME_ = "username_";

	public static Session getSession() {
		try {
			Subject subject = SecurityUtils.getSubject();
			Session session = subject.getSession(false);
			if (session == null) {
				session = subject.getSession();
			}
			if (session != null) {
				return session;
			}
			// subject.logout();
		} catch (InvalidSessionException e) {

		}
		return null;
	}
	public static UserRealm.Principal getPrincipal() {
		try {
			Subject subject = SecurityUtils.getSubject();
			UserRealm.Principal principal = (UserRealm.Principal) subject.getPrincipal();
			if (principal != null) {
				return principal;
			}
		} catch (UnavailableSecurityManagerException e) {

		} catch (InvalidSessionException e) {

		}
		return null;
	}


	public static void clearCache(User user) {
		if(user != null){
			CacheUtils.remove(USER_CACHE, USER_CACHE_ID_ + user.getId());
			CacheUtils.remove(USER_CACHE, USER_CACHE_USER_NAME_ + user.getUsername());
		}
	}
	public static User get(String id) {
		User user = (User) CacheUtils.get(USER_CACHE, USER_CACHE_ID_ + id);
		if (user == null) {
//			user = userService.selectById(id);

			if (user == null) {
				return null;
			}
			CacheUtils.put(USER_CACHE, USER_CACHE_ID_ + user.getId(), user);
			CacheUtils.put(USER_CACHE, USER_CACHE_USER_NAME_ + user.getUsername(), user);
		}
		return user;
	}
	public static User getUser() {
		UserRealm.Principal principal = getPrincipal();
		if (principal != null) {
			User user = get(principal.getId());
			if (user != null) {
				return user;
			}
			return new User();
		}
		// 如果没有登录，则返回实例化空的User对象。
		User user =	new User();
		user.setDefault();
		return user;
	}

	public static void clearCache() {
		UserUtils.clearCache(getUser());
	}

}
