package com.xaaef.molly.auth.jwt;

import com.xaaef.molly.auth.exception.JwtNoLoginException;
import com.xaaef.molly.common.enums.AdminFlag;
import com.xaaef.molly.common.enums.UserType;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * <p>
 * 安全服务工具类
 * </p>
 *
 * @author Wang Chen Chen
 * @version 1.0
 * @date 2021/7/5 10:50
 */

public class JwtSecurityUtils {

    /**
     * 获取Authentication
     */
    public static Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }


    /**
     * 经过认证！
     */
    public static boolean isAuthenticated() {
        var auth = getAuthentication();
        return auth != null && !(auth instanceof AnonymousAuthenticationToken) && auth.isAuthenticated();
    }


    /**
     * 当前登录的用户，是否 Master 系统的用户
     */
    public static boolean isMasterUser() {
        return JwtSecurityUtils.isAuthenticated() &&
                JwtSecurityUtils.getLoginUser().getUserType() == UserType.SYSTEM;
    }


    /**
     * 当前登录的用户，是否 管理员
     */
    public static boolean isAdminUser() {
        return JwtSecurityUtils.isAuthenticated() &&
                JwtSecurityUtils.getLoginUser().getAdminFlag() == AdminFlag.YES;
    }


    /**
     * 获取当前登录的用户 租户ID
     **/
    public static String getTenantId() {
        return getLoginUser().getTenantId();
    }


    /**
     * 获取用户 唯一登录ID
     **/
    public static String getLoginId() {
        return getLoginUser().getLoginId();
    }


    /**
     * 获取用户账户
     **/
    public static Long getUserId() {
        return getLoginUser().getUserId();
    }


    /**
     * 获取用户账户
     **/
    public static String getUsername() {
        return getLoginUser().getUsername();
    }


    /**
     * 获取用户
     **/
    public static JwtLoginUser getLoginUser() {
        if (isAuthenticated()) {
            return (JwtLoginUser) getAuthentication().getPrincipal();
        } else {
            throw new JwtNoLoginException("用户暂无登录！", new RuntimeException());
        }
    }


    /**
     * 设置用户
     **/
    public static void setLoginUser(JwtLoginUser loginUser) {
        if (loginUser == null) {
            SecurityContextHolder.clearContext();
        } else {
            var authentication = new UsernamePasswordAuthenticationToken(
                    loginUser, null, loginUser.getAuthorities());
            // 将用户信息，设置到 SecurityContext 中，可以在任何地方 使用 下面语句获取 获取 当前用户登录信息
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
    }


    private final static PasswordEncoder PASSWORD_ENCODER = new BCryptPasswordEncoder();

    public static PasswordEncoder getPasswordEncoder() {
        return PASSWORD_ENCODER;
    }

    /**
     * 生成BCryptPasswordEncoder密码
     *
     * @param password 密码
     * @return 加密字符串
     */
    public static String encryptPassword(String password) {
        return PASSWORD_ENCODER.encode(password);
    }

    /**
     * 判断密码是否相同
     *
     * @param rawPassword     真实密码
     * @param encodedPassword 加密后字符
     * @return 结果
     */
    public static boolean matchesPassword(String rawPassword, String encodedPassword) {
        return PASSWORD_ENCODER.matches(rawPassword, encodedPassword);
    }

}
